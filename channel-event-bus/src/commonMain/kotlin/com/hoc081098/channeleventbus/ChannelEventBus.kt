package com.hoc081098.channeleventbus

import com.hoc081098.channeleventbus.OptionWhenSendingToBusDoesNotExist.CREATE_NEW_BUS
import com.hoc081098.channeleventbus.OptionWhenSendingToBusDoesNotExist.DO_NOTHING
import com.hoc081098.channeleventbus.OptionWhenSendingToBusDoesNotExist.THROW_EXCEPTION
import com.hoc081098.channeleventbus.ValidationBeforeClosing.REQUIRE_BUS_IS_EMPTY
import com.hoc081098.channeleventbus.ValidationBeforeClosing.REQUIRE_BUS_IS_EXISTING
import com.hoc081098.channeleventbus.ValidationBeforeClosing.REQUIRE_FLOW_IS_NOT_COLLECTING
import kotlin.collections.set
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.getOrElse
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized as coroutinesSynchronized

/**
 * ## Multi-keys, multi-producers, single-consumer event bus backed by [Channel]s.
 *
 * - This bus is thread-safe to be used by multiple threads.
 *   It is safe to send events from multiple threads without any synchronization.
 *
 * - [ChannelEvent.Key] will be used to identify a bus for a specific type of events.
 *   Each bus has a [Channel] to send events to and a [Flow] to receive events from.
 *
 * - The [Channel] is unbounded [Channel.UNLIMITED] (default) or conflated [Channel.CONFLATED].
 *   The [Flow] is cold and only one collector is allowed at a time.
 *   This make sure all events are consumed.
 *
 * ## Basic usage:
 * ```kotlin
 * // Create your event type
 * data class AwesomeEvent(val payload: Int) : ChannelEvent<AwesomeEvent> {
 *   override val key get() = Key
 *   companion object Key : ChannelEventKey<AwesomeEvent>(AwesomeEvent::class)
 * }
 *
 * // Create your bus instance
 * val bus = ChannelEventBus()
 *
 * // Send events to the bus
 * bus.send(AwesomeEvent(1))
 * bus.send(AwesomeEvent(2))
 * bus.send(AwesomeEvent(3))
 *
 * // Receive events from the bus
 * bus.receiveAsFlow(AwesomeEvent)
 *   .collect { e: AwesomeEvent -> println(e) }
 * ```
 */
public sealed interface ChannelEventBus {
  /**
   * Send [event] to the bus identified by [ChannelEvent.key].
   *
   * When the bus associated with `event.key` does not exist, the behavior is determined by [option].
   * See [OptionWhenSendingToBusDoesNotExist] for more details.
   *
   * @throws ChannelEventBusException.SendException if failed to send the event.
   * @see OptionWhenSendingToBusDoesNotExist
   */
  @Throws(ChannelEventBusException.SendException::class)
  public fun <E : ChannelEvent<E>> send(
    event: E,
    option: OptionWhenSendingToBusDoesNotExist = CREATE_NEW_BUS,
  )

  /**
   * Receive events from the bus identified by [ChannelEvent.key].
   *
   * The returned [Flow] is cold and only one collector is allowed at a time.
   * This make sure all events are consumed.
   *
   * If you want to collect the flow multiple times, you must share the flow,
   * or must cancel the previous collection before collecting again with a new one.
   *
   * @throws ChannelEventBusException.FlowAlreadyCollected if collecting the flow is already collected
   * by another collector.
   */
  @Throws(ChannelEventBusException.FlowAlreadyCollected::class)
  public fun <T : ChannelEvent<T>> receiveAsFlow(key: ChannelEventKey<T>): Flow<T>

  /**
   * Close the bus identified by [key].
   *
   * You can validate the bus before closing by passing [validations].
   * By default, all validations are enabled. If you want to close the bus without any validation,
   * just pass an empty set or [ValidationBeforeClosing.NONE].
   *
   * @param key the key to identify the bus.
   * @param validations the validations to check before closing the bus.
   *
   * @throws ChannelEventBusException.CloseException if failed to close the bus.
   * @see ValidationBeforeClosing
   */
  @Throws(ChannelEventBusException.CloseException::class)
  public fun closeKey(
    key: ChannelEventKey<*>,
    validations: Set<ValidationBeforeClosing> = ValidationBeforeClosing.ALL,
  )

  /**
   * Close all buses without any validation.
   */
  public fun close()
}

/**
 * Create a new [ChannelEventBus] instance.
 *
 * @param logger a [ChannelEventBusLogger] to log events.
 */
public fun ChannelEventBus(logger: ChannelEventBusLogger? = null): ChannelEventBus = ChannelEventBusImpl(logger)

// ------------------------------------ INTERNAL ------------------------------------

@OptIn(InternalCoroutinesApi::class)
private class SynchronizedHashMap<K, V>(map: HashMap<K, V> = hashMapOf()) : MutableMap<K, V> by map {
  @JvmField
  val lock = SynchronizedObject()

  inline fun <T> synchronized(block: () -> T): T = coroutinesSynchronized(lock, block)
}

/**
 * A bus contains a [Channel] and a flag to indicate whether the channel is collecting or not.
 */
private class Bus private constructor(
  val channel: Channel<Any>,
  val isCollecting: Boolean,
) {
  fun copy(isCollecting: Boolean): Bus = Bus(channel, isCollecting)

  override fun hashCode() = 31 * channel.hashCode() + isCollecting.hashCode()

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Bus) return false
    return channel == other.channel && isCollecting == other.isCollecting
  }

  override fun toString(): String = "${super.toString()}($channel, $isCollecting)"

  companion object {
    @JvmStatic
    internal fun create(key: ChannelEventKey<*>, isCollecting: Boolean): Bus =
      Bus(key.createChannel(), isCollecting)
  }
}

private class ChannelEventBusImpl(
  @JvmField val logger: ChannelEventBusLogger?,
) : ChannelEventBus {
  /**
   * Guarded by [SynchronizedHashMap.lock].
   */
  private val _busMap = SynchronizedHashMap<ChannelEventKey<*>, Bus>()

  private fun getOrNull(key: ChannelEventKey<*>): Bus? = _busMap.synchronized { _busMap[key] }

  private fun getOrThrow(key: ChannelEventKey<*>): Bus = _busMap.synchronized {
    _busMap[key]
      ?: throw ChannelEventBusException.SendException.BusDoesNotExist(key)
  }

  private fun getOrCreateBus(key: ChannelEventKey<*>): Bus =
    _busMap.synchronized {
      _busMap.getOrPut(key) {
        Bus.create(key = key, isCollecting = false)
          .also { logger?.onCreated(key, this) }
      }
    }

  private fun getOrCreateBusAndMarkAsCollecting(key: ChannelEventKey<*>): Bus =
    _busMap.synchronized {
      val existing = _busMap[key]
      if (existing === null) {
        Bus.create(key = key, isCollecting = true)
          .also { _busMap[key] = it }
          .also { logger?.onCreated(key, this) }
      } else {
        if (existing.isCollecting) {
          throw ChannelEventBusException.FlowAlreadyCollected(key)
        }

        existing.copy(isCollecting = true)
          .also { _busMap[key] = it }
          .also { logger?.onStartCollection(key, this) }
      }
    }

  /**
   * Throws if there is no bus associated with [key].
   */
  private fun markAsNotCollecting(key: ChannelEventKey<*>): Unit =
    _busMap.synchronized {
      // _busMap[key] can be null if it is removed and closed before calling this method.
      // Just ignore in that case.
      _busMap[key] = (_busMap[key] ?: return)
        .copy(isCollecting = false)
        .also { logger?.onStopCollection(key, this) }
    }

  /**
   * @throws ChannelEventBusException.CloseException
   */
  @OptIn(ExperimentalCoroutinesApi::class)
  private fun removeBus(
    key: ChannelEventKey<*>,
    validations: Set<ValidationBeforeClosing>,
  ): Bus? = _busMap.synchronized {
    val removed = _busMap.remove(key)

    if (REQUIRE_BUS_IS_EXISTING in validations && removed === null) {
      throw ChannelEventBusException.CloseException.BusDoesNotExist(key)
    }

    removed?.also {
      if (REQUIRE_FLOW_IS_NOT_COLLECTING in validations && it.isCollecting) {
        throw ChannelEventBusException.CloseException.BusIsCollecting(key)
      }
      if (REQUIRE_BUS_IS_EMPTY in validations && !it.channel.isEmpty) {
        throw ChannelEventBusException.CloseException.BusIsNotEmpty(key)
      }
      logger?.onClosed(key, this)
    }
  }

  // ---------------------------------------------------------------------------------------------

  override fun <E : ChannelEvent<E>> send(
    event: E,
    option: OptionWhenSendingToBusDoesNotExist,
  ) {
    when (option) {
      CREATE_NEW_BUS -> getOrCreateBus(event.key)
      THROW_EXCEPTION -> getOrThrow(event.key)
      DO_NOTHING -> getOrNull(event.key) ?: return
    }
      .channel
      .trySend(event)
      .onSuccess { logger?.onSent(event, this) }
      .getOrElse { throw ChannelEventBusException.SendException.FailedToSendEvent(event, it) }
  }

  override fun <T : ChannelEvent<T>> receiveAsFlow(key: ChannelEventKey<T>): Flow<T> = flow {
    try {
      getOrCreateBusAndMarkAsCollecting(key)
        .channel
        .receiveAsFlow()
        .map(key::cast)
        .let { emitAll(it) }
    } catch (@Suppress("TooGenericExceptionCaught") e: Throwable) {
      markAsNotCollecting(key)
      throw e
    }

    // Normal completion
    markAsNotCollecting(key)
  }

  override fun closeKey(
    key: ChannelEventKey<*>,
    validations: Set<ValidationBeforeClosing>,
  ) {
    removeBus(
      key = key,
      validations = validations,
    )?.channel?.close()
  }

  override fun close() {
    _busMap.synchronized {
      val keys = logger?.let { _busMap.keys.toSet() }

      _busMap.values.forEach { it.channel.close() }
      _busMap.clear()

      logger?.onClosedAll(keys!!, this)
    }
  }
}

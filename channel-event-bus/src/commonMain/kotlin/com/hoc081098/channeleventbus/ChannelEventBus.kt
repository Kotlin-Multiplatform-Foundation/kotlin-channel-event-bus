package com.hoc081098.channeleventbus

import kotlin.collections.set
import kotlin.jvm.JvmField
import kotlin.reflect.cast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.getOrElse
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
 * - The [Channel] is unbounded [Channel.UNLIMITED].
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
   * @throws ChannelEventBusException.FailedToSendEvent if failed to send the event.
   */
  public fun <E : ChannelEvent<E>> send(event: E)

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
  public fun <T : ChannelEvent<T>> receiveAsFlow(key: ChannelEventKey<T>): Flow<T>

  /**
   * Close the bus identified by [key].
   *
   * You can validate the bus before closing by passing [requireNotCollecting], [requireChannelEmpty], [requireExists]
   * - If [requireNotCollecting] is `true`, the bus must not be collecting by any collector before closing.
   * - If [requireChannelEmpty] is `true`, the channel must be empty before closing.
   * - If [requireExists] is `true`, the bus must exist before closing.
   *
   * @param key the key to identify the bus.
   * @param requireNotCollecting require the bus must not be collecting by any collector before closing.
   * If `true` and the bus is collecting, [ChannelEventBusException.CloseException.BusIsCollecting] will be thrown.
   * @param requireChannelEmpty require the channel must be empty before closing.
   * If `true` and the channel is not empty, [ChannelEventBusException.CloseException.BusIsNotEmpty] will be thrown.
   * @param requireExists require the bus must exist before closing.
   * If `true` and the bus does not exist, [ChannelEventBusException.CloseException.BusDoesNotExist] will be thrown.
   *
   * @throws ChannelEventBusException.CloseException if failed to close the bus.
   */
  public fun closeKey(
    key: ChannelEventKey<*>,
    requireNotCollecting: Boolean = true,
    requireChannelEmpty: Boolean = false,
    requireExists: Boolean = true,
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
private data class Bus(
  val channel: Channel<Any>,
  val isCollecting: Boolean,
) {
  override fun toString(): String = "${super.toString()}($channel, $isCollecting)"
}

private class ChannelEventBusImpl(
  @JvmField val logger: ChannelEventBusLogger?,
) : ChannelEventBus {
  /**
   * Guarded by [SynchronizedHashMap.lock].
   */
  private val _busMap = SynchronizedHashMap<ChannelEventKey<*>, Bus>()

  private fun getOrCreateBus(key: ChannelEventKey<*>): Bus =
    _busMap.synchronized {
      _busMap.getOrPut(key) {
        Bus(channel = Channel(capacity = Channel.UNLIMITED), isCollecting = false)
          .also { logger?.onCreated(key, this) }
      }
    }

  private fun getOrCreateBusAndMarkAsCollecting(key: ChannelEventKey<*>): Bus =
    _busMap.synchronized {
      val existing = _busMap[key]
      if (existing === null) {
        Bus(channel = Channel(capacity = Channel.UNLIMITED), isCollecting = true)
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
      _busMap[key] = _busMap[key]!!
        .copy(isCollecting = false)
        .also { logger?.onStopCollection(key, this) }
    }

  /**
   * @throws ChannelEventBusException.CloseException
   */
  @OptIn(ExperimentalCoroutinesApi::class)
  private fun removeBus(
    key: ChannelEventKey<*>,
    requireNotCollecting: Boolean,
    requireChannelEmpty: Boolean,
    requireExists: Boolean,
  ): Bus? = _busMap.synchronized {
    val removed = _busMap.remove(key)

    if (requireExists) {
      if (removed === null) {
        throw ChannelEventBusException.CloseException.BusDoesNotExist(key)
      }
    }

    removed?.also {
      if (requireNotCollecting && it.isCollecting) {
        throw ChannelEventBusException.CloseException.BusIsCollecting(key)
      }
      if (requireChannelEmpty && !it.channel.isEmpty) {
        throw ChannelEventBusException.CloseException.BusIsNotEmpty(key)
      }
      logger?.onClosed(key, this)
    }
  }

  // ---------------------------------------------------------------------------------------------

  override fun <E : ChannelEvent<E>> send(event: E) = getOrCreateBus(event.key)
    .channel
    .trySend(event)
    .getOrElse { throw ChannelEventBusException.FailedToSendEvent(event, it) }
    .also { logger?.onSent(event, this) }

  override fun <T : ChannelEvent<T>> receiveAsFlow(key: ChannelEventKey<T>): Flow<T> = flow {
    try {
      getOrCreateBusAndMarkAsCollecting(key)
        .channel
        .receiveAsFlow()
        .map { key.eventClass.cast(it) }
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
    requireNotCollecting: Boolean,
    requireChannelEmpty: Boolean,
    requireExists: Boolean,
  ) {
    removeBus(
      key = key,
      requireNotCollecting = requireNotCollecting,
      requireChannelEmpty = requireChannelEmpty,
      requireExists = requireExists,
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

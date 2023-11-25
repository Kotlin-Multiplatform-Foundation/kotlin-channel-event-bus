package com.hoc081098.channeleventbus

import kotlin.jvm.JvmField
import kotlin.jvm.JvmSynthetic
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlinx.coroutines.channels.Channel

/**
 * Represents an event that can be sent to a [ChannelEventBus].
 */
public interface ChannelEvent<T : ChannelEvent<T>> {
  /**
   * The key to identify a bus for this type of events.
   */
  public val key: Key<T>

  /**
   * The [ChannelEvent.Key] to identify a bus for this type of events.
   *
   * @param T the type of events.
   * @param eventClass the [KClass] of events.
   * @param capacity the [ChannelEventBusCapacity] of the [Channel] associated with this key.
   * Default is [ChannelEventBusCapacity.UNLIMITED].
   *
   * @see [ChannelEventBusCapacity]
   */
  public open class Key<T : ChannelEvent<T>>(
    @JvmField
    internal val eventClass: KClass<T>,
    @JvmField
    internal val capacity: ChannelEventBusCapacity = ChannelEventBusCapacity.UNLIMITED,
  ) {
    final override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Key<*>) return false
      return eventClass == other.eventClass && capacity == other.capacity
    }

    final override fun hashCode(): Int = 31 * eventClass.hashCode() + capacity.hashCode()

    final override fun toString(): String = "ChannelEvent.Key(${eventClass.simpleName}, $capacity)"

    @JvmSynthetic
    internal inline fun cast(it: Any): T = eventClass.cast(it)

    @JvmSynthetic
    internal inline fun createChannel(): Channel<Any> = Channel(capacity.asInt())
  }
}

/**
 * Alias for [ChannelEvent.Key].
 * @see [ChannelEvent.Key]
 */
public typealias ChannelEventKey<T> = ChannelEvent.Key<T>

/**
 * Retrieve the [ChannelEvent.Key] for [T].
 *
 * You should cache the result of this function to avoid unnecessary object creation, for example:
 *
 * ```kotlin
 * @JvmField
 * val awesomeEventKey = channelEventKeyOf<AwesomeEvent>()
 * bus.receiveAsFlow(awesomeEventKey).collect { e: AwesomeEvent -> println(e) }
 * ```
 */
public inline fun <reified T : ChannelEvent<T>> channelEventKeyOf(
  capacity: ChannelEventBusCapacity = ChannelEventBusCapacity.UNLIMITED,
): ChannelEventKey<T> = ChannelEventKey(T::class, capacity)

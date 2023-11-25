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

  public open class Key<T : ChannelEvent<T>>(
    @JvmField
    internal val eventClass: KClass<T>,
    @JvmField
    internal val capacity: ChannelEventBusCapacity = ChannelEventBusCapacity.UNLIMITED,
  ) {
    final override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Key<*>) return false
      return eventClass == other.eventClass
    }

    final override fun hashCode(): Int = eventClass.hashCode()

    final override fun toString(): String = "ChannelEvent.Key(${eventClass.simpleName})"

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
public inline fun <reified T : ChannelEvent<T>> channelEventKeyOf(): ChannelEventKey<T> =
  ChannelEventKey(T::class)

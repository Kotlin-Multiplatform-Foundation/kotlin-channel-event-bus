package com.hoc081098.channeleventbus

import kotlin.reflect.KClass

/**
 * Represents an event that can be sent to a [ChannelEventBus].
 */
public interface ChannelEvent<T : ChannelEvent<T>> {
  /**
   * The key to identify a bus for this type of events.
   */
  public val key: Key<T>

  public open class Key<T : ChannelEvent<T>>(public val eventClass: KClass<T>) {
    final override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Key<*>) return false
      return eventClass == other.eventClass
    }

    final override fun hashCode(): Int = eventClass.hashCode()

    final override fun toString(): String = "ChannelEvent.Key(${eventClass.simpleName})"
  }
}

/**
 * Alias for [ChannelEvent.Key].
 * @see [ChannelEvent.Key]
 */
public typealias ChannelEventKey<T> = ChannelEvent.Key<T>

public inline fun <reified T : ChannelEvent<T>> channelEventKeyOf(): ChannelEventKey<T> =
  ChannelEventKey(T::class)

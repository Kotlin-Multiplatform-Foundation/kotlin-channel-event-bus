package com.hoc081098.channeleventbus

/**
 * Represents an event that can be sent to a [ChannelEventBus].
 */
public interface ChannelEvent<out T : ChannelEvent<T>> {
  public interface Key<
    @Suppress("unused")
    out T : ChannelEvent<T>,
    >

  /**
   * The key to identify a bus for this type of events.
   */
  public val key: Key<T>
}

internal typealias ChannelEventKey<T> = ChannelEvent.Key<T>

package com.hoc081098.channeleventbus

import kotlin.jvm.JvmSynthetic
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

@JvmSynthetic
internal inline fun ChannelEventBusCapacity.asInt(): Int {
  return when (this) {
    ChannelEventBusCapacity.UNLIMITED -> Channel.UNLIMITED
    ChannelEventBusCapacity.CONFLATED -> Channel.CONFLATED
  }
}

/**
 * The capacity of the [Channel] associated with a [ChannelEvent.Key].
 *
 * @see [ChannelEvent.Key.capacity]
 */
public enum class ChannelEventBusCapacity {
  /**
   * The [Channel] is unbounded [Channel.UNLIMITED].
   *
   * The [Channel] associated with a [ChannelEvent.Key] will has an unlimited capacity buffer.
   * This means that the [Channel] will never suspend the sender and will never drop elements.
   *
   * @see [Channel.UNLIMITED]
   */
  UNLIMITED,

  /**
   * The [Channel] is bounded [Channel.CONFLATED].
   *
   * The [Channel] associated with a [ChannelEvent.Key] will has a conflated capacity buffer.
   * The size of buffer is 1 and will keep only the most recently sent element (drop the oldest element).
   * This means that the [Channel] will never suspend the sender, but will drop the oldest element when buffer is full.
   *
   * @see [Channel.CONFLATED]
   * @see [BufferOverflow.DROP_OLDEST]
   */
  CONFLATED,
}

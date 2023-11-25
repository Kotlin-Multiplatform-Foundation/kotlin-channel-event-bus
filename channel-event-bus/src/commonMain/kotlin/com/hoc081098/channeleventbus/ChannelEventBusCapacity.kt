package com.hoc081098.channeleventbus

import kotlin.jvm.JvmSynthetic
import kotlinx.coroutines.channels.Channel

@JvmSynthetic
internal inline fun ChannelEventBusCapacity.asInt(): Int {
  return when (this) {
    ChannelEventBusCapacity.UNLIMITED -> Channel.UNLIMITED
    ChannelEventBusCapacity.CONFLATED -> Channel.CONFLATED
  }
}

public enum class ChannelEventBusCapacity {
  /**
   * The [Channel] is unbounded [Channel.UNLIMITED].
   */
  UNLIMITED,

  /**
   * The [Channel] is bounded [Channel.CONFLATED].
   */
  CONFLATED,
}

package com.hoc081098.channeleventbus.sample.android.ui.home

import com.hoc081098.channeleventbus.ChannelEvent
import com.hoc081098.channeleventbus.ChannelEventKey
import com.hoc081098.channeleventbus.sample.android.utils.NonBlankString

internal data class DetailResultToHomeEvent(val value: NonBlankString) : ChannelEvent<DetailResultToHomeEvent> {
  override val key get() = Key

  companion object Key : ChannelEventKey<DetailResultToHomeEvent>(DetailResultToHomeEvent::class)
}

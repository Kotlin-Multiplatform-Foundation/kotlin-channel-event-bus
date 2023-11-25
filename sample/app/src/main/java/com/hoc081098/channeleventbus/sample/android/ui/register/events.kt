package com.hoc081098.channeleventbus.sample.android.ui.register

import com.hoc081098.channeleventbus.ChannelEvent
import com.hoc081098.channeleventbus.ChannelEventKey

internal data class SubmitFirstNameEvent(val value: String?) : ChannelEvent<SubmitFirstNameEvent> {
  override val key get() = Key

  companion object Key : ChannelEventKey<SubmitFirstNameEvent>(SubmitFirstNameEvent::class)
}

internal data class SubmitLastNameEvent(val value: String?) : ChannelEvent<SubmitLastNameEvent> {
  override val key get() = Key

  companion object Key : ChannelEventKey<SubmitLastNameEvent>(SubmitLastNameEvent::class)
}

internal data class SubmitGenderEvent(val value: Gender?) : ChannelEvent<SubmitGenderEvent> {
  override val key get() = Key

  companion object Key : ChannelEventKey<SubmitGenderEvent>(SubmitGenderEvent::class)
}

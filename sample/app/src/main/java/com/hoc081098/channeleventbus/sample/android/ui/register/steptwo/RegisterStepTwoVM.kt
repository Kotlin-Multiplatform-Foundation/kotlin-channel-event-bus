package com.hoc081098.channeleventbus.sample.android.ui.register.steptwo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.sample.android.ui.register.Gender
import com.hoc081098.channeleventbus.sample.android.ui.register.SubmitGenderEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class RegisterStepTwoVM(
  private val savedStateHandle: SavedStateHandle,
  private val channelEventBus: ChannelEventBus,
) : ViewModel() {
  internal val genderStateFlow: StateFlow<Gender> = savedStateHandle
    .getStateFlow(GenderKey, Gender.MALE)

  init {
    sendSubmitGenderEventAfterChanged()
  }

  /**
   * Send [SubmitGenderEvent] to [ChannelEventBus] when [genderStateFlow] emits a new value
   */
  private fun sendSubmitGenderEventAfterChanged() {
    genderStateFlow
      .onEach { channelEventBus.send(SubmitGenderEvent(it)) }
      .onCompletion {
        check(it is CancellationException) { "Expected CancellationException but was $it" }

        channelEventBus.send(SubmitGenderEvent(null))
      }
      .launchIn(viewModelScope)
  }

  internal fun onGenderChanged(value: Gender) {
    savedStateHandle[GenderKey] = value
  }

  companion object {
    private const val GenderKey = "gender"
  }
}

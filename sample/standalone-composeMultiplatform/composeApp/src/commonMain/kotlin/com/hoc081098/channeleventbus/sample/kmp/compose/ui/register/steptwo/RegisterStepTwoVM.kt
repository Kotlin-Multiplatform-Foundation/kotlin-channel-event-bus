package com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.steptwo

import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.OptionWhenSendingToBusDoesNotExist
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.Gender
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.GenderKey
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.SubmitGenderEvent
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.safe.safe
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class RegisterStepTwoVM(
  private val savedStateHandle: SavedStateHandle,
  private val channelEventBus: ChannelEventBus,
) : ViewModel() {
  internal val genderStateFlow: StateFlow<Gender?> = savedStateHandle.safe.getStateFlow(GenderKey)

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

        // Send null to bus when this ViewModel is cleared, to clear the value in RegisterSharedVM.
        // Do nothing if the bus does not exist (ie. there is no active collector for this bus or the bus is closed).
        channelEventBus.send(
          event = SubmitGenderEvent(null),
          option = OptionWhenSendingToBusDoesNotExist.DO_NOTHING,
        )
      }
      .launchIn(viewModelScope)
  }

  internal fun onGenderChanged(value: Gender) {
    savedStateHandle.safe[GenderKey] = value
  }
}

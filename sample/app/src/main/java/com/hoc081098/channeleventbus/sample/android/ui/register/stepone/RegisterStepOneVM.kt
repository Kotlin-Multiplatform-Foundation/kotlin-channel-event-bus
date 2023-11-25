package com.hoc081098.channeleventbus.sample.android.ui.register.stepone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.OptionWhenSendingToBusDoesNotExist
import com.hoc081098.channeleventbus.sample.android.common.SafeSavedStateHandle
import com.hoc081098.channeleventbus.sample.android.ui.register.FirstNameKey
import com.hoc081098.channeleventbus.sample.android.ui.register.LastNameKey
import com.hoc081098.channeleventbus.sample.android.ui.register.SubmitFirstNameEvent
import com.hoc081098.channeleventbus.sample.android.ui.register.SubmitLastNameEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class RegisterStepOneVM(
  savedStateHandle: SavedStateHandle,
  private val channelEventBus: ChannelEventBus,
) : ViewModel() {
  private val safeSavedStateHandle = SafeSavedStateHandle(savedStateHandle)

  internal val firstNameStateFlow: StateFlow<String?> = safeSavedStateHandle.getStateFlow(FirstNameKey)
  internal val lastNameStateFlow: StateFlow<String?> = safeSavedStateHandle.getStateFlow(LastNameKey)

  init {
    sendSubmitFirstNameEventAfterChanged()
    sendSubmitLastNameEventAfterChanged()
  }

  /**
   * Send [SubmitFirstNameEvent] to [ChannelEventBus] when [firstNameStateFlow] emits a new value
   */
  private fun sendSubmitFirstNameEventAfterChanged() {
    firstNameStateFlow
      .onEach { channelEventBus.send(SubmitFirstNameEvent(it)) }
      .onCompletion {
        check(it is CancellationException) { "Expected CancellationException but was $it" }

        // Send null to bus when this ViewModel is cleared, to clear the value in RegisterSharedVM.
        // Do nothing if the bus does not exist (ie. there is no active collector for this bus or the bus is closed).
        channelEventBus.send(
          event = SubmitFirstNameEvent(null),
          option = OptionWhenSendingToBusDoesNotExist.DO_NOTHING,
        )
      }
      .launchIn(viewModelScope)
  }

  /**
   * Send [SubmitLastNameEvent] to [ChannelEventBus] when [lastNameStateFlow] emits a new value
   */
  private fun sendSubmitLastNameEventAfterChanged() {
    lastNameStateFlow
      .onEach { channelEventBus.send(SubmitLastNameEvent(it)) }
      .onCompletion {
        check(it is CancellationException) { "Expected CancellationException but was $it" }

        // Send null to bus when this ViewModel is cleared, to clear the value in RegisterSharedVM.
        // Do nothing if the bus does not exist (ie. there is no active collector for this bus or the bus is closed).
        channelEventBus.send(
          event = SubmitLastNameEvent(null),
          option = OptionWhenSendingToBusDoesNotExist.DO_NOTHING,
        )
      }
      .launchIn(viewModelScope)
  }

  internal fun onFirstNameChanged(value: String) {
    safeSavedStateHandle[FirstNameKey] = value
  }

  internal fun onLastNameChanged(value: String) {
    safeSavedStateHandle[LastNameKey] = value
  }
}

package com.hoc081098.channeleventbus.sample.android.ui.register.stepone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusOptionWhenSendingToBusDoesNotExist
import com.hoc081098.channeleventbus.sample.android.ui.register.SubmitFirstNameEvent
import com.hoc081098.channeleventbus.sample.android.ui.register.SubmitLastNameEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class RegisterStepOneVM(
  private val savedStateHandle: SavedStateHandle,
  private val channelEventBus: ChannelEventBus,
) : ViewModel() {
  internal val firstNameStateFlow: StateFlow<String?> = savedStateHandle
    .getStateFlow<String?>(FirstNameKey, null)

  internal val lastNameStateFlow: StateFlow<String?> = savedStateHandle
    .getStateFlow<String?>(LastNameKey, null)

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
        // Do nothing if the bus does not exist (ie. there is no active collector for this bus).
        channelEventBus.send(
          event = SubmitFirstNameEvent(null),
          option = ChannelEventBusOptionWhenSendingToBusDoesNotExist.DO_NOTHING,
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
        // Do nothing if the bus does not exist (ie. there is no active collector for this bus).
        channelEventBus.send(
          event = SubmitLastNameEvent(null),
          option = ChannelEventBusOptionWhenSendingToBusDoesNotExist.DO_NOTHING,
        )
      }
      .launchIn(viewModelScope)
  }

  internal fun onFirstNameChanged(value: String) {
    savedStateHandle[FirstNameKey] = value
  }

  internal fun onLastNameChanged(value: String) {
    savedStateHandle[LastNameKey] = value
  }

  companion object {
    private const val FirstNameKey = "first_name"
    private const val LastNameKey = "last_name"
  }
}

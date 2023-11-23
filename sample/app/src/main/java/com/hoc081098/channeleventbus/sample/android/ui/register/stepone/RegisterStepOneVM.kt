package com.hoc081098.channeleventbus.sample.android.ui.register.stepone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.sample.android.ui.register.SubmitFirstNameEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class RegisterStepOneVM(
  private val savedStateHandle: SavedStateHandle,
  private val channelEventBus: ChannelEventBus,
) : ViewModel() {
  internal val firstNameStateFlow: StateFlow<String?> = savedStateHandle.getStateFlow<String?>(FIRST_NAME_KEY, null)

  init {
    firstNameStateFlow
      .onEach { channelEventBus.send(SubmitFirstNameEvent(it)) }
      .onCompletion {
        check(it is CancellationException)
        channelEventBus.send(SubmitFirstNameEvent(null))
      }
      .launchIn(viewModelScope)
  }

  internal fun submitFirstName(value: String) {
    savedStateHandle[FIRST_NAME_KEY] = value
  }

  companion object {
    const val FIRST_NAME_KEY = "first_name"
  }
}

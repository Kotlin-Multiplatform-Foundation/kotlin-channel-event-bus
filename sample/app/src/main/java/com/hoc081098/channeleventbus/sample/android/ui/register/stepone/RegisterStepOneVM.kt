package com.hoc081098.channeleventbus.sample.android.ui.register.stepone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.sample.android.ui.register.SubmitFirstNameEvent
import kotlinx.coroutines.flow.StateFlow

class RegisterStepOneVM(
  private val savedStateHandle: SavedStateHandle,
  private val channelEventBus: ChannelEventBus,
) : ViewModel() {
  internal val firstNameStateFlow: StateFlow<String?> = savedStateHandle.getStateFlow<String?>(FIRST_NAME_KEY, null)

  init {
    addCloseable { channelEventBus.send(SubmitFirstNameEvent(null)) }
  }

  internal fun submitFirstName(value: String) {
    savedStateHandle[FIRST_NAME_KEY] = value
    channelEventBus.send(SubmitFirstNameEvent(value))
  }

  companion object {
    const val FIRST_NAME_KEY = "first_name"
  }
}

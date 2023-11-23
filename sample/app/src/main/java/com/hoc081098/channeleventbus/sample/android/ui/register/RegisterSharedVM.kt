package com.hoc081098.channeleventbus.sample.android.ui.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusValidationBeforeClosing.Companion.NONE
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RegisterSharedVM(
  private val channelEventBus: ChannelEventBus,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  init {
    addCloseable {
      channelEventBus.closeKey(
        key = SubmitFirstNameEvent,
        validations = NONE,
      )
    }

    channelEventBus
      .receiveAsFlow(SubmitFirstNameEvent)
      .onEach { savedStateHandle["first_name"] = it }
      .launchIn(viewModelScope)
  }
}

package com.hoc081098.channeleventbus.sample.android.ui.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusValidationBeforeClosing.Companion.NONE
import kotlin.LazyThreadSafetyMode.PUBLICATION
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class RegisterSharedVM(
  private val channelEventBus: ChannelEventBus,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  init {
    Timber.d("$this::init")

    addCloseable {
      Timber.d("$this::close")
      channelEventBus.closeKey(
        key = SubmitFirstNameEvent,
        validations = NONE,
      )
    }
    savedStateHandle
      .getStateFlow(FirstNameKey, null as String?)
      .onEach { Timber.d("$this FirstNameKey -> $it") }
      .launchIn(viewModelScope)

    channelEventBus
      .receiveAsFlow(SubmitFirstNameEvent)
      .onEach { savedStateHandle[FirstNameKey] = it.value }
      .launchIn(viewModelScope)
  }

  private companion object {
    val FirstNameKey by lazy(PUBLICATION) { SubmitFirstNameEvent.toString() }
  }
}

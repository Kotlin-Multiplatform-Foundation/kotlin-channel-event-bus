package com.hoc081098.channeleventbus.sample.android.ui.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ValidationBeforeClosing.Companion.NONE
import com.hoc081098.channeleventbus.sample.android.BuildConfig
import com.hoc081098.channeleventbus.sample.android.common.SafeSavedStateHandle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class RegisterSharedVM(
  private val channelEventBus: ChannelEventBus,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val safeSavedStateHandle = SafeSavedStateHandle(savedStateHandle)

  internal val uiStateFlow: StateFlow<RegisterUiState> = combine(
    safeSavedStateHandle.getStateFlow(FirstNameKey),
    safeSavedStateHandle.getStateFlow(LastNameKey),
    safeSavedStateHandle.getStateFlow(GenderKey),
  ) { firstName, lastName, gender ->
    RegisterUiState.from(
      firstName = firstName,
      lastName = lastName,
      gender = gender,
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = @Suppress("MagicNumber") 5_000),
    initialValue = RegisterUiState.Unfilled,
  )

  init {
    Timber.d("$this::init")
    addCloseable {
      arrayOf(SubmitFirstNameEvent, SubmitLastNameEvent, SubmitGenderEvent).forEach {
        channelEventBus.closeKey(key = it, validations = NONE)
      }
      Timber.d("$this::close")
    }

    debugPrint()
    observeSubmitFirstNameEvent()
    observeSubmitLastNameEvent()
    observeSubmitGenderEvent()
  }

  private fun observeSubmitLastNameEvent() {
    channelEventBus
      .receiveAsFlow(SubmitLastNameEvent)
      .onEach { safeSavedStateHandle[LastNameKey] = it.value }
      .launchIn(viewModelScope)
  }

  private fun observeSubmitFirstNameEvent() {
    channelEventBus
      .receiveAsFlow(SubmitFirstNameEvent)
      .onEach { safeSavedStateHandle[FirstNameKey] = it.value }
      .launchIn(viewModelScope)
  }

  private fun observeSubmitGenderEvent() {
    channelEventBus
      .receiveAsFlow(SubmitGenderEvent)
      .onEach { safeSavedStateHandle[GenderKey] = it.value }
      .launchIn(viewModelScope)
  }

  private fun debugPrint() {
    if (!BuildConfig.DEBUG) {
      return
    }

    val line = "-".repeat(@Suppress("MagicNumber") 80)

    combine(
      safeSavedStateHandle.getStateFlow(FirstNameKey),
      safeSavedStateHandle.getStateFlow(LastNameKey),
      safeSavedStateHandle.getStateFlow(GenderKey),
    ) { array: Array<*> -> array.joinToString(separator = "\n") { "    $it" } }
      .distinctUntilChanged()
      .onEach {
        Timber.d(
          """
          |$this::debugPrint $line
          |$it
          |$line
          """.trimMargin(),
        )
      }
      .launchIn(viewModelScope)
  }
}

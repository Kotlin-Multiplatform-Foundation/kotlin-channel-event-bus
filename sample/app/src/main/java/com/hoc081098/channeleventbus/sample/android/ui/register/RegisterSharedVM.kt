package com.hoc081098.channeleventbus.sample.android.ui.register

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusValidationBeforeClosing.Companion.NONE
import kotlin.LazyThreadSafetyMode.PUBLICATION
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@Immutable
enum class Gender {
  MALE,
  FEMALE,
}

class RegisterSharedVM(
  private val channelEventBus: ChannelEventBus,
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  init {
    Timber.d("$this::init")
    addCloseable {
      arrayOf(SubmitFirstNameEvent, SubmitLastNameEvent).forEach {
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
      .onEach { savedStateHandle[LastNameKey] = it.value }
      .launchIn(viewModelScope)
  }

  private fun observeSubmitFirstNameEvent() {
    channelEventBus
      .receiveAsFlow(SubmitFirstNameEvent)
      .onEach { savedStateHandle[FirstNameKey] = it.value }
      .launchIn(viewModelScope)
  }

  private fun observeSubmitGenderEvent() {
    channelEventBus
      .receiveAsFlow(SubmitGenderEvent)
      .onEach { savedStateHandle[GenderKey] = it.value }
      .launchIn(viewModelScope)
  }

  private fun debugPrint() {
    val line = "-".repeat(80)

    combine(
      listOf(
        FirstNameKey,
        LastNameKey,
        GenderKey,
      ).map { savedStateHandle.getStateFlow(it, null as Any?) },
    ) { array -> array.joinToString(separator = "\n") { "    $it" } }
      .distinctUntilChanged()
      .onEach {
        Timber.d(
          """
          |$this::debugPrint
          |$it
          |$line
          """.trimMargin(),
        )
      }
      .launchIn(viewModelScope)
  }

  private companion object {
    private val FirstNameKey by lazy(PUBLICATION) { SubmitFirstNameEvent.toString() }
    private val LastNameKey by lazy(PUBLICATION) { SubmitLastNameEvent.toString() }
    private val GenderKey by lazy(PUBLICATION) { SubmitGenderEvent.toString() }
  }
}

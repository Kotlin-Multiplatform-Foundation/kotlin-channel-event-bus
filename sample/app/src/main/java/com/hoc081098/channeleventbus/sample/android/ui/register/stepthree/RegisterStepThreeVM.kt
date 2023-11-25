package com.hoc081098.channeleventbus.sample.android.ui.register.stepthree

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.sample.android.ui.register.RegisterUiState
import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.startWith
import java.io.IOException
import kotlin.random.Random
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@Immutable
internal sealed interface RegisterStepThreeSingleEvent {
  data object Success : RegisterStepThreeSingleEvent
  data class Failure(val throwable: Throwable) : RegisterStepThreeSingleEvent
}

@Immutable
internal sealed interface RegisterStepThreeUiState {
  data object Idle : RegisterStepThreeUiState
  data object Registering : RegisterStepThreeUiState
  data object Success : RegisterStepThreeUiState
  data class Failure(val throwable: Throwable) : RegisterStepThreeUiState
}

class RegisterStepThreeVM(
  private val savedStateHandle: SavedStateHandle,
  private val channelEventBus: ChannelEventBus,
) : ViewModel() {
  private val _registerFlow = MutableSharedFlow<RegisterUiState.Filled>(extraBufferCapacity = 1)

  private val _eventChannel = Channel<RegisterStepThreeSingleEvent>(capacity = Channel.UNLIMITED)
  internal val eventFlow: Flow<RegisterStepThreeSingleEvent> = _eventChannel.receiveAsFlow()

  internal val uiStateFlow: StateFlow<RegisterStepThreeUiState> = _registerFlow
    .flatMapFirst { state ->
      flowFromSuspend { doRegister(state) }
        .map { RegisterStepThreeUiState.Success }
        .startWith(RegisterStepThreeUiState.Registering)
        .catch { emit(RegisterStepThreeUiState.Idle) }
    }
    .onEach(::sendEvent)
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Eagerly,
      initialValue = RegisterStepThreeUiState.Idle,
    )

  private fun sendEvent(state: RegisterStepThreeUiState) {
    when (state) {
      RegisterStepThreeUiState.Idle, RegisterStepThreeUiState.Registering ->
        Unit

      is RegisterStepThreeUiState.Failure ->
        _eventChannel.trySend(RegisterStepThreeSingleEvent.Failure(state.throwable))

      RegisterStepThreeUiState.Success ->
        _eventChannel.trySend(RegisterStepThreeSingleEvent.Success)
    }
  }

  private suspend fun doRegister(state: RegisterUiState.Filled) {
    Timber.d("doRegister $state")

    // simulate network request
    delay(2_000)
    if (Random.nextBoolean()) {
      throw IOException("Network error")
        .also { Timber.e(it, "Register failed") }
    } else {
      Timber.d("Register success")
    }
  }

  internal fun register(state: RegisterUiState) {
    when (state) {
      RegisterUiState.Unfilled -> {
        Timber.w("Unfilled state")
        return
      }

      is RegisterUiState.Filled -> {
        viewModelScope.launch { _registerFlow.emit(state) }
      }
    }
  }

  companion object {
  }
}

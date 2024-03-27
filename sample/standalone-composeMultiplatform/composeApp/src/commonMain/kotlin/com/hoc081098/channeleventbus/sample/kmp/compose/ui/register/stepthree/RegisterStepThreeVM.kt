package com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepthree

import androidx.compose.runtime.Immutable
import com.hoc081098.channeleventbus.sample.kmp.compose.common.HasSingleEventFlow
import com.hoc081098.channeleventbus.sample.kmp.compose.common.SingleEventChannel
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.RegisterUiState
import com.hoc081098.flowext.FlowExtPreview
import com.hoc081098.flowext.catchAndReturn
import com.hoc081098.flowext.flatMapFirst
import com.hoc081098.flowext.flowFromSuspend
import com.hoc081098.flowext.mapTo
import com.hoc081098.flowext.startWith
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import java.io.IOException
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Immutable
sealed interface RegisterStepThreeSingleEvent {
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

@OptIn(FlowExtPreview::class)
class RegisterStepThreeVM(
  private val singleEventChannel: SingleEventChannel<RegisterStepThreeSingleEvent>,
) : ViewModel(singleEventChannel),
  HasSingleEventFlow<RegisterStepThreeSingleEvent> by singleEventChannel {
  private val _registerFlow = MutableSharedFlow<RegisterUiState.Filled>(extraBufferCapacity = 1)

  internal val uiStateFlow: StateFlow<RegisterStepThreeUiState> = _registerFlow
    .flatMapFirst { state ->
      flowFromSuspend { doRegister(state) }
        .mapTo(RegisterStepThreeUiState.Success)
        .startWith(RegisterStepThreeUiState.Registering)
        .catchAndReturn(RegisterStepThreeUiState.Idle)
    }
    .onEach(::sendEvent)
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Eagerly,
      initialValue = RegisterStepThreeUiState.Idle,
    )

  private suspend fun sendEvent(state: RegisterStepThreeUiState) = when (state) {
    RegisterStepThreeUiState.Idle, RegisterStepThreeUiState.Registering ->
      Unit

    is RegisterStepThreeUiState.Failure ->
      singleEventChannel.sendEvent(RegisterStepThreeSingleEvent.Failure(state.throwable))

    RegisterStepThreeUiState.Success ->
      singleEventChannel.sendEvent(RegisterStepThreeSingleEvent.Success)
  }

  internal fun register(state: RegisterUiState) {
    when (state) {
      RegisterUiState.Unfilled -> {
        Napier.w("Unfilled state")
        return
      }

      is RegisterUiState.Filled -> {
        viewModelScope.launch { _registerFlow.emit(state) }
      }
    }
  }
}

private suspend fun doRegister(state: RegisterUiState.Filled) {
  Napier.d("doRegister $state")

  // simulate network request
  delay(@Suppress("MagicNumber") 2_000)

  if (Random.nextBoolean()) {
    Napier.e("Register failed")
    throw IOException("Network error")
  } else {
    Napier.d("Register success")
  }
}

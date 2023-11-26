package com.hoc081098.channeleventbus.sample.android.ui.home.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.sample.android.common.SafeSavedStateHandle
import com.hoc081098.channeleventbus.sample.android.common.SavedStateHandleKey
import com.hoc081098.channeleventbus.sample.android.ui.home.DetailResultToHomeEvent
import kotlinx.coroutines.flow.StateFlow

class DetailVM(
  private val channelEventBus: ChannelEventBus,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val safeSavedStateHandle = SafeSavedStateHandle(savedStateHandle)

  internal val textStateFlow: StateFlow<String> = safeSavedStateHandle.getStateFlow(TextKey)

  internal fun onTextChanged(text: String) {
    safeSavedStateHandle[TextKey] = text
  }

  internal fun sendResultToHome() {
    val value = textStateFlow.value.takeIf { it.isNotBlank() } ?: return
    channelEventBus.send(DetailResultToHomeEvent(value))
  }

  private companion object {
    private val TextKey = SavedStateHandleKey("text", "")
  }
}

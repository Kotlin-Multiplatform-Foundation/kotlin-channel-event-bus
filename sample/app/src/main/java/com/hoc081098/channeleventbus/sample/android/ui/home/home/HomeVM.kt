package com.hoc081098.channeleventbus.sample.android.ui.home.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.sample.android.ui.home.DetailResultToHomeEvent
import com.hoc081098.channeleventbus.sample.android.utils.NonBlankString
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class HomeVM(
  channelEventBus: ChannelEventBus,
) : ViewModel() {
  internal val detailResultsStateFlow: StateFlow<PersistentList<NonBlankString>> = channelEventBus
    .receiveAsFlow(DetailResultToHomeEvent)
    .onCompletion {
      check(it is CancellationException) { "Expected CancellationException but was $it" }
      // Close the bus when this ViewModel is cleared.
      channelEventBus.closeKey(DetailResultToHomeEvent)
    }
    .map { it.value }
    .scan(persistentListOf<NonBlankString>()) { acc, value ->
      if (value in acc) {
        acc
      } else {
        acc + value
      }
    }
    .stateIn(
      scope = viewModelScope,
      // Using SharingStarted.Lazily is enough, because the event bus is backed by a channel.
      started = SharingStarted.Lazily,
      initialValue = persistentListOf(),
    )
}

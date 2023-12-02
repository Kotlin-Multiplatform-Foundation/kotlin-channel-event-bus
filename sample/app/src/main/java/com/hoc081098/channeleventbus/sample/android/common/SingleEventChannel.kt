package com.hoc081098.channeleventbus.sample.android.common

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import java.io.Closeable
import java.lang.System.identityHashCode
import kotlin.LazyThreadSafetyMode.NONE
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

sealed interface SingleEventFlow<E> : Flow<E> {
  /**
   * Must collect in [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * Safe to call in the coroutines launched by [androidx.lifecycle.lifecycleScope].
   *
   * In Compose, we can use [CollectWithLifecycleEffect] with `inImmediateMain = true`.
   */
  @MainThread
  override suspend fun collect(collector: FlowCollector<E>)
}

@MainThread
interface HasSingleEventFlow<E> {
  /**
   * Must collect in [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * Safe to call in the coroutines launched by [androidx.lifecycle.lifecycleScope].
   *
   * In Compose, we can use [CollectWithLifecycleEffect] with `inImmediateMain = true`.
   */
  val singleEventFlow: SingleEventFlow<E>
}

@MainThread
sealed interface SingleEventFlowSender<E> {
  /**
   * Must call in [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * Safe to call in the coroutines launched by [androidx.lifecycle.viewModelScope].
   */
  suspend fun sendEvent(event: E)
}

private class SingleEventFlowImpl<E>(private val channel: Channel<E>) : SingleEventFlow<E> {
  override suspend fun collect(collector: FlowCollector<E>) {
    debugCheckImmediateMainDispatcher()
    return collector.emitAll(channel.receiveAsFlow())
  }
}

@MainThread
class SingleEventChannel<E> :
  Closeable,
  HasSingleEventFlow<E>,
  SingleEventFlowSender<E> {
  private val _eventChannel = Channel<E>(Channel.UNLIMITED)

  override val singleEventFlow: SingleEventFlow<E> by lazy(NONE) { SingleEventFlowImpl(_eventChannel) }

  init {
    Timber.d("[EventChannel] created: hashCode=${identityHashCode(this)}")
  }

  /**
   * Must be called in Dispatchers.Main.immediate, otherwise it will throw an exception.
   * If you want to send an event from other Dispatcher,
   * use `withContext(Dispatchers.Main.immediate) { eventChannel.send(event) }`
   */
  @MainThread
  override suspend fun sendEvent(event: E) {
    debugCheckImmediateMainDispatcher()

    _eventChannel
      .trySend(event)
      .onClosed { return }
      .onFailure { Timber.e(it, "[EventChannel] Failed to send event: $event, hashCode=${identityHashCode(this)}") }
      .onSuccess { Timber.d("[EventChannel] Sent event: $event, hashCode=${identityHashCode(this)}") }
  }

  override fun close() {
    _eventChannel.close()
    Timber.d("[EventChannel] closed: hashCode=${identityHashCode(this)}")
  }
}

@Suppress("NOTHING_TO_INLINE")
inline fun <E> SingleEventChannel<E>.addToViewModel(viewModel: ViewModel) =
  apply { viewModel.addCloseable(this) }

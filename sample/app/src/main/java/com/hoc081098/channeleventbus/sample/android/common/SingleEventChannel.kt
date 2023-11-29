package com.hoc081098.channeleventbus.sample.android.common

import androidx.annotation.MainThread
import com.hoc081098.channeleventbus.sample.android.BuildConfig
import java.io.Closeable
import java.lang.System.identityHashCode
import kotlin.LazyThreadSafetyMode.NONE
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

@MainThread
interface HasSingleEventFlow<E> {
  /**
   * Must collect in [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * Safe to call in the coroutines launched by [androidx.lifecycle.lifecycleScope].
   */
  val eventFlow: Flow<E>
}

@MainThread
interface SingleEventFlowSender<E> {
  /**
   * Must call in [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
   * Safe to call in the coroutines launched by [androidx.lifecycle.viewModelScope].
   */
  suspend fun send(event: E)
}

@MainThread
class SingleEventChannel<E> constructor() :
  Closeable,
  HasSingleEventFlow<E>,
  SingleEventFlowSender<E> {
  private val _eventChannel = Channel<E>(Channel.UNLIMITED)

  override val eventFlow: Flow<E> by lazy(NONE) {
    if (BuildConfig.DEBUG) {
      flow {
        debugCheckImmediateMainDispatcher()
        emitAll(_eventChannel.receiveAsFlow())
      }
    } else {
      _eventChannel.receiveAsFlow()
    }
  }

  init {
    Timber.d("[EventChannel] created: hashCode=${identityHashCode(this)}")
  }

  /**
   * Must be called in Dispatchers.Main.immediate, otherwise it will throw an exception.
   * If you want to send an event from other Dispatcher,
   * use `withContext(Dispatchers.Main.immediate) { eventChannel.send(event) }`
   */
  @MainThread
  override suspend fun send(event: E) {
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

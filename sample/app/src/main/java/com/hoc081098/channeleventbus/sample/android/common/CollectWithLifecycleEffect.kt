package com.hoc081098.channeleventbus.sample.android.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Collect the given [Flow] in an effect that runs when [LifecycleOwner.lifecycle] is at least at [minActiveState].
 *
 * If [inImmediateMain] is `true`, the effect will run in
 * [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate],
 * otherwise it will run in [androidx.compose.runtime.Composer.applyCoroutineContext].
 *
 * @param keys Keys to be used to [remember] the effect.
 * @param lifecycleOwner The [LifecycleOwner] to be used to [repeatOnLifecycle].
 * @param minActiveState The minimum [Lifecycle.State] to be used to [repeatOnLifecycle].
 * @param inImmediateMain Whether the effect should run in
 * [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate].
 * @param collector The collector to be used to collect the [Flow].
 *
 * @see [LaunchedEffect]
 */
@Composable
fun <T> Flow<T>.CollectWithLifecycleEffect(
  vararg keys: Any?,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
  inImmediateMain: Boolean = true,
  collector: (T) -> Unit,
) {
  val flow = this
  val currentCollector by rememberUpdatedState(collector)

  val block: suspend CoroutineScope.() -> Unit = {
    lifecycleOwner.repeatOnLifecycle(minActiveState) {
      flow.collect(currentCollector)
    }
  }

  if (inImmediateMain) {
    LaunchedEffectInImmediateMain(flow, lifecycleOwner, minActiveState, *keys, block = block)
  } else {
    LaunchedEffect(flow, lifecycleOwner, minActiveState, *keys, block = block)
  }
}

@Composable
@NonRestartableComposable
@Suppress("ArrayReturn")
private fun LaunchedEffectInImmediateMain(
  vararg keys: Any?,
  block: suspend CoroutineScope.() -> Unit,
) {
  remember(*keys) { LaunchedEffectImpl(block) }
}

private class LaunchedEffectImpl(
  private val task: suspend CoroutineScope.() -> Unit,
) : RememberObserver {
  private val scope = CoroutineScope(Dispatchers.Main.immediate)
  private var job: Job? = null

  override fun onRemembered() {
    job?.cancel("Old job was still running!")
    job = scope.launch(block = task)
  }

  override fun onForgotten() {
    job?.cancel()
    job = null
  }

  override fun onAbandoned() {
    job?.cancel()
    job = null
  }
}

package com.hoc081098.channeleventbus.sample.android.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Launch a coroutine immediately to collect the flow.
 * It is a shortcut for `scope.launch(CoroutineStart.UNDISPATCHED) { flow.collect() }`.
 *
 * This differs from [kotlinx.coroutines.flow.launchIn] in that the collection is started immediately
 * _in the current thread_
 * until the first suspension point, without dispatching to the [CoroutineDispatcher] of the scope context.
 * However, when the coroutine is resumed from suspension, it is dispatched to the [CoroutineDispatcher] in its context.
 *
 * This is useful when collecting a [kotlinx.coroutines.flow.SharedFlow] which does not replay or buffer values,
 * and you don't want to miss any values due to the dispatching to the [CoroutineDispatcher].
 *
 * @see kotlinx.coroutines.flow.launchIn
 * @see CoroutineStart.UNDISPATCHED
 */
fun <T> Flow<T>.launchNowIn(scope: CoroutineScope): Job =
  scope.launch(start = CoroutineStart.UNDISPATCHED) {
    collect() // tail-call
  }

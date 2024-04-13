package com.hoc081098.channeleventbus.sample.kmp.compose.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext

expect fun isBuildDebug(): Boolean

@OptIn(ExperimentalStdlibApi::class)
suspend inline fun debugCheckImmediateMainDispatcher() {
  if (isBuildDebug()) {
    val dispatcher = currentCoroutineContext()[CoroutineDispatcher]!!

    check(
      dispatcher === Dispatchers.Main.immediate ||
        !dispatcher.isDispatchNeeded(Dispatchers.Main.immediate),
    ) {
      "Expected CoroutineDispatcher to be Dispatchers.Main.immediate but was $dispatcher"
    }
  }
}

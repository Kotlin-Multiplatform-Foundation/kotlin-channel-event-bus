package com.hoc081098.channeleventbus.sample.android.common

import com.hoc081098.channeleventbus.sample.android.BuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext

@OptIn(ExperimentalStdlibApi::class)
suspend inline fun debugCheckImmediateMainDispatcher() {
  if (BuildConfig.DEBUG) {
    val dispatcher = currentCoroutineContext()[CoroutineDispatcher]
    check(dispatcher === Dispatchers.Main.immediate) {
      "Expected CoroutineDispatcher to be Dispatchers.Main.immediate but was $dispatcher"
    }
  }
}

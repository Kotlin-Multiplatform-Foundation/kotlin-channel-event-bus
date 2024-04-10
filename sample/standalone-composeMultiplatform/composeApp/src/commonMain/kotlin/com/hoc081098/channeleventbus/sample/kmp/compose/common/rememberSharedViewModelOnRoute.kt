package com.hoc081098.channeleventbus.sample.kmp.compose.common

import androidx.compose.runtime.Composable
import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelStore
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import com.hoc081098.kmp.viewmodel.koin.compose.koinKmpViewModel
import com.hoc081098.solivagant.navigation.BaseRoute
import com.hoc081098.solivagant.navigation.rememberCloseableOnRoute

@Composable
inline fun <reified T : ViewModel> koinSharedViewModelOnRoute(route: BaseRoute): T {
  val viewModelStoreOwner = rememberCloseableOnRoute(
    route = route,
    factory = { SharedVMStoreOwner() },
  )
  return koinKmpViewModel<T>(viewModelStoreOwner = viewModelStoreOwner)
}

@PublishedApi
internal class SharedVMStoreOwner : ViewModelStoreOwner, Closeable {
  @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
  // TODO: When Solivagant supports getting ViewModelStoreOwner by NavRoute, remove this workaround.
  override val viewModelStore: ViewModelStore = com.hoc081098.solivagant.navigation.internal.createViewModelStore()

  override fun close() = viewModelStore.clear()
}

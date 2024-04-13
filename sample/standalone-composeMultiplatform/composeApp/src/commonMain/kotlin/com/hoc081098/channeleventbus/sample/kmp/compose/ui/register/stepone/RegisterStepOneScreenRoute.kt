package com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepone

import androidx.compose.runtime.Immutable
import com.hoc081098.channeleventbus.sample.kmp.compose.common.koinSharedViewModelOnRoute
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavDestination
import com.hoc081098.solivagant.navigation.NavRoot
import com.hoc081098.solivagant.navigation.ScreenDestination

@Immutable
@Parcelize
data object RegisterStepOneScreenRoute : NavRoot

@JvmField
val RegisterStepOneScreenDestination: NavDestination =
  ScreenDestination<RegisterStepOneScreenRoute> { _, modifier ->
    RegisterStepOneScreen(
      registerSharedVM = koinSharedViewModelOnRoute(route = RegisterStepOneScreenRoute),
      modifier = modifier,
    )
  }

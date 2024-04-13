package com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepthree

import androidx.compose.runtime.Immutable
import com.hoc081098.channeleventbus.sample.kmp.compose.common.koinSharedViewModelOnRoute
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepone.RegisterStepOneScreenRoute
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavDestination
import com.hoc081098.solivagant.navigation.NavRoute
import com.hoc081098.solivagant.navigation.ScreenDestination

@Immutable
@Parcelize
data object RegisterStepThreeScreenRoute : NavRoute

@JvmField
val RegisterStepThreeScreenDestination: NavDestination =
  ScreenDestination<RegisterStepThreeScreenRoute> { route, modifier ->
    RegisterStepThreeScreen(
      registerSharedVM = koinSharedViewModelOnRoute(route = RegisterStepOneScreenRoute),
      modifier = modifier,
    )
  }

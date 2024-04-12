package com.hoc081098.channeleventbus.sample.kmp.compose.ui.home.home

import androidx.compose.runtime.Immutable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavDestination
import com.hoc081098.solivagant.navigation.NavRoot
import com.hoc081098.solivagant.navigation.ScreenDestination

@Immutable
@Parcelize
data object HomeScreenRoute : NavRoot

@JvmField
val HomeScreenRouteDestination: NavDestination =
  ScreenDestination<HomeScreenRoute> { _, modifier ->
    HomeScreen(
      modifier = modifier,
    )
  }

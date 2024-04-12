package com.hoc081098.channeleventbus.sample.kmp.compose.ui.home.detail

import androidx.compose.runtime.Immutable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize
import com.hoc081098.solivagant.navigation.NavDestination
import com.hoc081098.solivagant.navigation.NavRoute
import com.hoc081098.solivagant.navigation.ScreenDestination

@Immutable
@Parcelize
data object DetailScreenRoute : NavRoute

@JvmField
val DetailScreenDestination: NavDestination =
  ScreenDestination<DetailScreenRoute> { _, modifier ->
    DetailScreen(
      modifier = modifier,
    )
  }

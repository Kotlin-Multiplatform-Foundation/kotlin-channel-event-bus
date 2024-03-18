package com.hoc081098.channeleventbus.sample.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun rememberCurrentRouteAsState(currentBackStackEntryAsState: State<NavBackStackEntry?>): State<Route?> =
  remember(currentBackStackEntryAsState) {
    derivedStateOf {
      currentBackStackEntryAsState.value
        ?.destination
        ?.route
        ?.let(Route::ofOrNull)
    }
  }

@Immutable
internal sealed class Route {
  @Stable
  abstract val routePattern: String

  @Stable
  fun matches(route: String): Boolean = route == routePattern

  data object RegisterStepOne : Route() {
    override val routePattern = "register_step_one"
    val route = routePattern
  }

  data object RegisterStepTwo : Route() {
    override val routePattern = "register_step_two"
    val route = routePattern
  }

  data object RegisterStepThree : Route() {
    override val routePattern = "register_step_three"
    val route = routePattern
  }

  data object Home : Route() {
    override val routePattern = "home"
    val route = routePattern
  }

  data object Detail : Route() {
    override val routePattern = "detail"
    val route = routePattern
  }

  companion object {
    private val VALUES: ImmutableList<Route> by lazy {
      persistentListOf(
        RegisterStepOne,
        RegisterStepTwo,
        RegisterStepThree,
        Home,
        Detail,
      )
    }

    @Stable
    fun ofOrNull(route: String): Route? = VALUES.singleOrNull { it.matches(route) }
  }
}

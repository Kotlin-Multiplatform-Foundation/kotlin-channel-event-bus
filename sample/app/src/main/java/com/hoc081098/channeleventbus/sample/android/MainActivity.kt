@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.hoc081098.channeleventbus.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.hoc081098.channeleventbus.sample.android.common.MyApplicationTheme
import com.hoc081098.channeleventbus.sample.android.ui.home.HomeScreen
import com.hoc081098.channeleventbus.sample.android.ui.register.stepone.RegisterStepOneScreen
import com.hoc081098.channeleventbus.sample.android.ui.register.stepthree.RegisterStepThreeScreen
import com.hoc081098.channeleventbus.sample.android.ui.register.steptwo.RegisterStepTwoScreen
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalLayoutApi::class)
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
        ) {
          val navController = rememberNavController()
          val currentBackStackEntryAsState = navController.currentBackStackEntryAsState()

          val route by rememberCurrentRouteAsState(currentBackStackEntryAsState)
          val previousBackStackEntry by remember(currentBackStackEntryAsState) {
            derivedStateOf {
              currentBackStackEntryAsState.value
              navController.previousBackStackEntry
            }
          }

          Scaffold(
            topBar = {
              TopAppBar(
                title = {
                  Text(
                    text = when (route) {
                      Route.RegisterStepOne -> "Register step 1"
                      Route.RegisterStepTwo -> "Register step 2"
                      Route.RegisterStepThree -> "Register step 3"
                      Route.Home -> "Home"
                      null -> ""
                    },
                  )
                },
                navigationIcon = {
                  if (previousBackStackEntry != null) {
                    IconButton(onClick = navController::popBackStack) {
                      Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                      )
                    }
                  }
                },
              )
            },
          ) { innerPadding ->
            AppNavHost(
              modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize(),
              navController = navController,
            )
          }
        }
      }
    }
  }
}

@Suppress("LongMethod")
@Composable
private fun AppNavHost(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
) {
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = "register_graph",
  ) {
    navigation(startDestination = Route.RegisterStepOne.routePattern, route = "register_graph") {
      composable(route = Route.RegisterStepOne.routePattern) { entry ->
        val registerGraphEntry = remember(entry) { navController.getBackStackEntry("register_graph") }

        DisposableEffect(registerGraphEntry) {
          Timber.d("RegisterStepOneScreen: $registerGraphEntry")
          onDispose { }
        }

        RegisterStepOneScreen(
          registerSharedVM = koinViewModel(viewModelStoreOwner = registerGraphEntry),
          navigateToRegisterStepTwo = remember(navController) {
            {
              navController.navigate(route = Route.RegisterStepTwo.route)
            }
          },
        )
      }

      composable(route = Route.RegisterStepTwo.routePattern) { entry ->
        val registerGraphEntry = remember(entry) { navController.getBackStackEntry("register_graph") }

        DisposableEffect(registerGraphEntry) {
          Timber.d("RegisterStepTwoScreen: $registerGraphEntry")
          onDispose { }
        }

        RegisterStepTwoScreen(
          registerSharedVM = koinViewModel(viewModelStoreOwner = registerGraphEntry),
          navigateToRegisterStepThree = remember(navController) {
            {
              navController.navigate(route = Route.RegisterStepThree.route)
            }
          },
        )
      }

      composable(route = Route.RegisterStepThree.routePattern) { entry ->
        val registerGraphEntry = remember(entry) { navController.getBackStackEntry("register_graph") }

        DisposableEffect(registerGraphEntry) {
          Timber.d("RegisterStepThreeScreen: $registerGraphEntry")
          onDispose { }
        }

        RegisterStepThreeScreen(
          registerSharedVM = koinViewModel(viewModelStoreOwner = registerGraphEntry),
          navigateToHome = remember(navController) {
            {
              navController.navigate(
                route = Route.Home.route,
                navOptions = navOptions {
                  popUpTo(navController.graph.id) {
                    saveState = false
                    inclusive = true
                  }
                  launchSingleTop = true
                  restoreState = false
                },
              )
            }
          },
        )
      }
    }

    navigation(startDestination = Route.Home.routePattern, route = "home_graph") {
      composable(route = Route.Home.routePattern) {
        HomeScreen()
      }
    }
  }
}

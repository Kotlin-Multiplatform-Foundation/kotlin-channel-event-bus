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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.hoc081098.channeleventbus.sample.android.common.MyApplicationTheme
import com.hoc081098.channeleventbus.sample.android.ui.register.stepone.RegisterStepOneScreen

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
                      Route.RegisterStepThree -> "Register step 2"
                      Route.RegisterStepTwo -> "Register step 3"
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
    navigation(startDestination = "register_graph", route = Route.RegisterStepOne.routePattern) {
      composable(route = Route.RegisterStepOne.routePattern) {
        RegisterStepOneScreen()
      }

      composable(route = Route.RegisterStepTwo.routePattern) {
      }

      composable(route = Route.RegisterStepThree.routePattern) {
      }
    }
  }
}

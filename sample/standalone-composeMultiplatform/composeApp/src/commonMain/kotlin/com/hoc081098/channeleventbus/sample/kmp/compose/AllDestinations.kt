package com.hoc081098.channeleventbus.sample.kmp.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.hoc081098.channeleventbus.sample.kmp.compose.common.MyApplicationTheme
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepone.RegisterStepOneScreenDestination
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepone.RegisterStepOneScreenRoute
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepthree.RegisterStepThreeScreenDestination
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.steptwo.RegisterStepTwoScreenDestination
import com.hoc081098.solivagant.navigation.BaseRoute
import com.hoc081098.solivagant.navigation.NavDestination
import com.hoc081098.solivagant.navigation.NavEventNavigator
import com.hoc081098.solivagant.navigation.NavHost
import com.hoc081098.solivagant.navigation.NavHostDefaults
import com.hoc081098.solivagant.navigation.NavRoot
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.adapters.ImmutableSetAdapter
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Stable
private val AllDestinations: ImmutableSet<NavDestination> by lazy {
  ImmutableSetAdapter(
    hashSetOf(
      RegisterStepOneScreenDestination,
      RegisterStepTwoScreenDestination,
      RegisterStepThreeScreenDestination,
    ),
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelEventBusSampleApp(
  modifier: Modifier = Modifier,
  navigator: NavEventNavigator = koinInject(),
) {
  var currentRoute by rememberSaveable { mutableStateOf<BaseRoute?>(null) }

  KoinContext {
    MyApplicationTheme(darkTheme = false) {
      Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
      ) {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {
            CenterAlignedTopAppBar(
              title = { Text(text = currentRoute.toString()) },
              navigationIcon = {
                if (currentRoute !is NavRoot) {
                  IconButton(onClick = remember { navigator::navigateBack }) {
                    Icon(
                      imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                      contentDescription = "Back",
                    )
                  }
                }
              },
            )
          },
        ) { innerPadding ->
          NavHost(
            modifier = Modifier.fillMaxSize()
              .padding(innerPadding)
              .consumeWindowInsets(innerPadding),
            startRoute = RegisterStepOneScreenRoute,
            destinations = AllDestinations,
            navEventNavigator = navigator,
            destinationChangedCallback = { currentRoute = it },
            transitionAnimations = NavHostDefaults.transitionAnimations(
              enterTransition = {
                slideIntoContainer(
                  towards = AnimatedContentTransitionScope.SlideDirection.Left,
                )
              },
              exitTransition = {
                slideOutOfContainer(
                  towards = AnimatedContentTransitionScope.SlideDirection.Left,
                )
              },
              popEnterTransition = {
                slideIntoContainer(
                  towards = AnimatedContentTransitionScope.SlideDirection.Right,
                )
              },
              popExitTransition = {
                slideOutOfContainer(
                  towards = AnimatedContentTransitionScope.SlideDirection.Right,
                )
              },
              replaceEnterTransition = { fadeIn() },
              replaceExitTransition = { fadeOut() },
            ),
          )
        }
      }
    }
  }
}

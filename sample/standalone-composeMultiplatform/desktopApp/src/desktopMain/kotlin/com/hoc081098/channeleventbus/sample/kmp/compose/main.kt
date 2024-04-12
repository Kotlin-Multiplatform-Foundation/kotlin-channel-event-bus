package com.hoc081098.channeleventbus.sample.kmp.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hoc081098.solivagant.lifecycle.LocalLifecycleOwner
import com.hoc081098.solivagant.navigation.ClearOnDispose
import com.hoc081098.solivagant.navigation.ExperimentalSolivagantApi
import com.hoc081098.solivagant.navigation.ProvideCompositionLocals
import com.hoc081098.solivagant.navigation.SavedStateSupport
import com.hoc081098.solivagant.navigation.rememberWindowLifecycleOwner
import org.koin.core.logger.Level

@OptIn(ExperimentalSolivagantApi::class)
fun main() {
  startKoinCommon {
    printLogger(Level.DEBUG)
  }

  val savedStateSupport = SavedStateSupport()
  application {
    savedStateSupport.ClearOnDispose()

    Window(
      onCloseRequest = ::exitApplication,
      title = "ChannelEventBus-Multiplatform-Sample",
    ) {
      val windowLifecycleOwner = rememberWindowLifecycleOwner()!!

      savedStateSupport.ProvideCompositionLocals(LocalLifecycleOwner provides windowLifecycleOwner) {
        ChannelEventBusSampleApp()
      }
    }
  }
}

@Preview
@Composable
fun AppDesktopPreview() {
  ChannelEventBusSampleApp()
}

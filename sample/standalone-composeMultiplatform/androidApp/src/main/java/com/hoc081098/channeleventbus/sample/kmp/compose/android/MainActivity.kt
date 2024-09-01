package com.hoc081098.channeleventbus.sample.kmp.compose.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hoc081098.channeleventbus.sample.kmp.compose.ChannelEventBusSampleApp
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      KoinAndroidContext {
        ChannelEventBusSampleApp()
      }
    }
  }
}

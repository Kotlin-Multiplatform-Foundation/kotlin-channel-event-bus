package com.hoc081098.channeleventbus.sample.android

import RegisterModule
import android.app.Application
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ConsoleChannelEventBusLogger
import com.hoc081098.channeleventbus.EmptyChannelEventBusLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApp : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@MyApp)
      androidLogger(
        if (BuildConfig.DEBUG) {
          Level.DEBUG
        } else {
          Level.ERROR
        },
      )

      modules(
        module {
          single {
            ChannelEventBus(
              if (BuildConfig.DEBUG) {
                ConsoleChannelEventBusLogger
              } else {
                EmptyChannelEventBusLogger
              },
            )
          }
        },
        RegisterModule,
      )
    }
  }
}

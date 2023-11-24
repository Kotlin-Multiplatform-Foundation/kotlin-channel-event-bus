package com.hoc081098.channeleventbus.sample.android

import RegisterModule
import android.app.Application
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber

class MyApp : Application() {
  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

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
                ChannelEventBusLogger.stdout()
              } else {
                ChannelEventBusLogger.noop()
              },
            )
          }
        },
        RegisterModule,
      )
    }
  }
}

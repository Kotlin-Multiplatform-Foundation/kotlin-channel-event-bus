package com.hoc081098.channeleventbus.sample.android

import android.app.Application
import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusLogger
import com.hoc081098.channeleventbus.sample.android.common.SingleEventChannel
import com.hoc081098.channeleventbus.sample.android.ui.home.HomeModule
import com.hoc081098.channeleventbus.sample.android.ui.register.RegisterModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber

val ChannelEventBusModule = module {
  single {
    ChannelEventBus(
      if (BuildConfig.DEBUG) {
        ChannelEventBusLogger.stdout()
      } else {
        ChannelEventBusLogger.noop()
      },
    )
  }
}

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
        ChannelEventBusModule,
        RegisterModule,
        HomeModule,
        module {
          factory { SingleEventChannel<Any?>() }
        },
      )
    }
  }
}

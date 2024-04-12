package com.hoc081098.channeleventbus.sample.kmp.compose

import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusLogger
import com.hoc081098.channeleventbus.sample.kmp.compose.common.SingleEventChannel
import com.hoc081098.channeleventbus.sample.kmp.compose.common.isBuildDebug
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.home.HomeModule
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.RegisterModule
import com.hoc081098.solivagant.navigation.NavEventNavigator
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin
import org.koin.core.module.KoinApplicationDslMarker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val ChannelEventBusModule = module {
  single {
    ChannelEventBus(
      if (isBuildDebug()) {
        ChannelEventBusLogger.stdout()
      } else {
        ChannelEventBusLogger.noop()
      },
    )
  }

  factory { SingleEventChannel<Any?>() }
}

val NavigatorModule = module {
  singleOf(::NavEventNavigator)
}

val CommonModule = module {
  includes(
    ChannelEventBusModule,
    RegisterModule,
    HomeModule,
    NavigatorModule,
  )
}

@KoinApplicationDslMarker
fun startKoinCommon(appDeclaration: KoinAppDeclaration = {}) {
  startKoin {
    appDeclaration()
    modules(CommonModule)
  }
}

fun setupNapier() {
  if (isBuildDebug()) {
    Napier.base(DebugAntilog())
  }
}

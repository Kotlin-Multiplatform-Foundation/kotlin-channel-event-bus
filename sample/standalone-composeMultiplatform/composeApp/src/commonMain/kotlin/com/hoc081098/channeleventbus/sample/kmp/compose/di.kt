package com.hoc081098.channeleventbus.sample.kmp.compose

import com.hoc081098.channeleventbus.ChannelEventBus
import com.hoc081098.channeleventbus.ChannelEventBusLogger
import com.hoc081098.channeleventbus.sample.kmp.compose.common.SingleEventChannel
import com.hoc081098.channeleventbus.sample.kmp.compose.common.isBuildDebug
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

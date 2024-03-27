package com.hoc081098.channeleventbus.sample.kmp.compose.ui.home

import com.hoc081098.channeleventbus.sample.kmp.compose.ui.home.detail.DetailVM
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.home.home.HomeVM
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val HomeModule = module {
  factoryOf(::DetailVM)
  factoryOf(::HomeVM)
}

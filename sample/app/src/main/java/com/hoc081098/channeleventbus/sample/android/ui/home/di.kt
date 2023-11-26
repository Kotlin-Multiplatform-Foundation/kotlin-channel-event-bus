package com.hoc081098.channeleventbus.sample.android.ui.home

import com.hoc081098.channeleventbus.sample.android.ui.home.detail.DetailVM
import com.hoc081098.channeleventbus.sample.android.ui.home.home.HomeVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val HomeModule = module {
  viewModelOf(::DetailVM)
  viewModelOf(::HomeVM)
}

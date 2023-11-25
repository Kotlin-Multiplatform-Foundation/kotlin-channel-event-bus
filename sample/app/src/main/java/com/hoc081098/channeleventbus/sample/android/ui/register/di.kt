package com.hoc081098.channeleventbus.sample.android.ui.register

import com.hoc081098.channeleventbus.sample.android.ui.register.stepone.RegisterStepOneVM
import com.hoc081098.channeleventbus.sample.android.ui.register.stepthree.RegisterStepThreeVM
import com.hoc081098.channeleventbus.sample.android.ui.register.steptwo.RegisterStepTwoVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val RegisterModule = module {
  viewModelOf(::RegisterSharedVM)
  viewModelOf(::RegisterStepOneVM)
  viewModelOf(::RegisterStepTwoVM)
  viewModelOf(::RegisterStepThreeVM)
}

package com.hoc081098.channeleventbus.sample.kmp.compose.ui.register

import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepone.RegisterStepOneVM
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepthree.RegisterStepThreeVM
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.steptwo.RegisterStepTwoVM
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val RegisterModule = module {
  factoryOf(::RegisterSharedVM)
  factoryOf(::RegisterStepOneVM)
  factoryOf(::RegisterStepTwoVM)
  factoryOf(::RegisterStepThreeVM)
}

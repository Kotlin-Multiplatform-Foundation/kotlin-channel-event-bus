import com.hoc081098.channeleventbus.sample.android.ui.register.RegisterSharedVM
import com.hoc081098.channeleventbus.sample.android.ui.register.stepone.RegisterStepOneVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val RegisterModule = module {
  viewModelOf(::RegisterSharedVM)
  viewModelOf(::RegisterStepOneVM)
}

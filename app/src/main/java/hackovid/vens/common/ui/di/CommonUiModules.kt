package hackovid.vens.common.ui.di

import hackovid.vens.common.ui.SharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonUiModule = module {
    viewModel { SharedViewModel(get(), get(), get()) }
}

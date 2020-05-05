package hackovid.vens.features.register.di

import hackovid.vens.features.register.RegisterViewModel
import hackovid.vens.features.register.SelectStoreViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registerModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { SelectStoreViewModel() }
}

package hackovid.vens.features.register.di

import hackovid.vens.features.register.FillStoreInfoViewModel
import hackovid.vens.features.register.RegisterFieldsValidator
import hackovid.vens.features.register.RegisterViewModel
import hackovid.vens.features.register.SelectStoreViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val registerModule = module {
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { FillStoreInfoViewModel(null, get(), get()) }
    viewModel { SelectStoreViewModel(get { parametersOf(true, false) }) }

    factory { RegisterFieldsValidator() }
}

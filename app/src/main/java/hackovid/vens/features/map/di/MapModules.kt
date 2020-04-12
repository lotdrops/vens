package hackovid.vens.features.map.di

import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.features.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val mapModule = module {
    viewModel { (sharedViewModel: SharedViewModel) ->
        MapViewModel(sharedViewModel, get { parametersOf(false) }, get())
    } }

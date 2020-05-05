package hackovid.vens.features.list.di

import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.features.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val listModule = module {
    viewModel { (sharedViewModel: SharedViewModel) ->
        ListViewModel(sharedViewModel, get { parametersOf(true, false) }, get(), get())
    }
}

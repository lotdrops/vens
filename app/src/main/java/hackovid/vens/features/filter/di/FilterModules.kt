package hackovid.vens.features.filter.di

import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.features.filter.FilterBottomSheetViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val filterModule = module {
    viewModel { (sharedViewModel: SharedViewModel) ->
        FilterBottomSheetViewModel(sharedViewModel, get())
    }
}

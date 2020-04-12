package hackovid.vens.features.detail.di

import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.features.detail.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { (sharedViewModel: SharedViewModel, storeId: Int) ->
        DetailViewModel(sharedViewModel, get(), storeId)
    }
}

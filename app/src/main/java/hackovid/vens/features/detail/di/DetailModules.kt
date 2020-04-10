package hackovid.vens.features.detail.di

import hackovid.vens.features.detail.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel { (storeId: Int) -> DetailViewModel(get(), storeId) }
}

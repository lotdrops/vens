package hackovid.vens.features.favourites.di

import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.features.favourites.FavouritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val favouritesModule = module {
    viewModel { (sharedViewModel: SharedViewModel) ->
        FavouritesViewModel(sharedViewModel, get { parametersOf(true, true) }, get(), get())
    }
}

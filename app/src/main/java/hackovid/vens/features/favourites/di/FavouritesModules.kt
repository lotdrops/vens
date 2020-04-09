package hackovid.vens.features.favourites.di

import hackovid.vens.features.favourites.FavouritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favouritesModule = module {
    viewModel { FavouritesViewModel(get()) }
}

package hackovid.vens.features.list

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hackovid.vens.R
import hackovid.vens.common.data.Favourite
import hackovid.vens.common.data.FavouriteDao
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.data.filter.SortStrategy
import hackovid.vens.common.data.filter.StoresUseCase
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.StoreListUi
import hackovid.vens.common.ui.toListUi
import hackovid.vens.common.ui.toLocation
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class ListViewModel(
    sharedViewModel: SharedViewModel,
    private val localStorage: LocalStorage,
    private val storesUseCase: StoresUseCase,
    private val favouriteDao: FavouriteDao
) : ViewModel() {
    private val _onlyFavourites = MutableLiveData(localStorage.getListIsFavourites())
    val onlyFavourites: LiveData<Boolean> get() = _onlyFavourites

    val screenTitle = onlyFavourites.map {
        if (it) R.string.favourites_screen_title else R.string.list_screen_title
    }
    val emptyListTitle = onlyFavourites.map {
        if (it) R.string.favourites_screen_empty_title else R.string.list_empty_title
    }
    val emptyListSubtitle = onlyFavourites.map {
        if (it) R.string.favourites_screen_empty_body else R.string.list_empty_message
    }

    private val location = sharedViewModel.location.map { it.toLocation() }
    private val queryParams = sharedViewModel.filter.combineWith(location) { filter, location ->
        filter?.let { Pair(it, location) }
    }
    private val paramsAndFavourite = onlyFavourites.combineWith(queryParams) {
            onlyFavourite, params -> Pair(onlyFavourite, params)
    }
    val stores = paramsAndFavourite.switchMap { it ->
        val onlyFavourites = it.first
        val queryParams = it.second
        if (onlyFavourites == true) {
            storesUseCase.getData(queryParams.toFavouritesFilterParams()).map { stores ->
                stores.map { store -> store.toListUi(location.value) }
            }
        } else {
            storesUseCase.getData(queryParams).map { storesAndFavourites ->
                storesAndFavourites.map { store -> store.toListUi(location.value) }
            }
        }
    }

    val showEmpty = stores.map { it.isEmpty() }

    fun onFavouriteClicked(item: StoreListUi) {
        viewModelScope.launch {
            val favourite = Favourite(item.id)
            if (item.isFavourite) favouriteDao.removeFavourite(favourite)
            else favouriteDao.addFavourite(favourite)
        }
    }

    fun onFavouritesClicked() {
        val newOnlyFavourites = onlyFavourites.value == false
        _onlyFavourites.value = newOnlyFavourites
        localStorage.setListIsFavourites(newOnlyFavourites)
    }

    private fun Pair<FilterParams, Location?>?.toFavouritesFilterParams() =
        this?.noFiltering().byName().favouritesOnly()

    private fun Pair<FilterParams, Location?>.noFiltering() =
        this.copy(first = FilterParams.createWithoutFiltering())

    private fun Pair<FilterParams, Location?>?.byName() =
        if (this == null) null
        else Pair(this.first.copy(sortStrategy = SortStrategy.NAME), second)

    private fun Pair<FilterParams, Location?>?.favouritesOnly() =
        if (this == null) null
        else Pair(this.first.copy(favouritesOnly = true), second)
}

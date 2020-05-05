package hackovid.vens.features.favourites

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.Favourite
import hackovid.vens.common.data.FavouriteDao
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.data.filter.SortStrategy
import hackovid.vens.common.data.filter.StoresUseCase
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.StoreListUi
import hackovid.vens.common.ui.toListUi
import hackovid.vens.common.ui.toLocation
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class FavouritesViewModel(
    sharedViewModel: SharedViewModel,
    private val storesUseCase: StoresUseCase,
    private val storeDao: StoreDao,
    private val favouriteDao: FavouriteDao
) : ViewModel() {
    private val location = sharedViewModel.location.map { it.toLocation() }
    private val queryParams = sharedViewModel.filter.combineWith(location) { filter, location ->
        filter?.let { Pair(it, location) }
    }
    val stores = queryParams.switchMap {
        storesUseCase.getData(it.byName()).map { stores ->
            stores.map { store -> store.toListUi(location.value) }
        }
    }

    val showEmpty: LiveData<Boolean> = stores.map { it.isEmpty() }

    fun onFavouriteClicked(item: StoreListUi) {
        viewModelScope.launch {
            val fab = Favourite( item.id)
            if(item.isFavourite) favouriteDao.removeFavourite(fab) else favouriteDao.addFavourite(fab)
        }
    }

    private fun Pair<FilterParams, Location?>?.byName() = if (this == null) null
    else Pair(this.first.copy(sortStrategy = SortStrategy.NAME), second)
}

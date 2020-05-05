package hackovid.vens.features.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.Favourite
import hackovid.vens.common.data.FavouriteDao
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.filter.StoresUseCase
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.StoreListUi
import hackovid.vens.common.ui.toListUi
import hackovid.vens.common.ui.toLocation
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class ListViewModel(
    sharedViewModel: SharedViewModel,
    private val storesUseCase: StoresUseCase,
    private val storeDao: StoreDao,
    private val favouriteDao: FavouriteDao
) : ViewModel() {
    private val location = sharedViewModel.location.map { it.toLocation() }
    private val queryParams = sharedViewModel.filter.combineWith(location) { filter, location ->
        filter?.let { Pair(it, location) }
    }
    val stores = queryParams.switchMap { it ->
        storesUseCase.getData(it).map { it.map { store -> store.toListUi(location.value) } }
    }

    fun onFavouriteClicked(item: StoreListUi) {
        viewModelScope.launch {
            val fav = Favourite(item.id)
            if(item.isFavourite) favouriteDao.removeFavourite(fav) else favouriteDao.addFavourite(fav)
        }
    }
}

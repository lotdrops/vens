package hackovid.vens.features.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.filter.StoresDataSource
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.StoreListUi
import hackovid.vens.common.ui.toListUi
import kotlinx.coroutines.launch

class FavouritesViewModel(
    sharedViewModel: SharedViewModel,
    private val storesDataSource: StoresDataSource,
    private val storeDao: StoreDao
) : ViewModel() {
    // TODO pass location to toListUi(loc)
    val stores = sharedViewModel.filter.switchMap {
        storesDataSource.getData(Pair(it, null)).map { it.map { store -> store.toListUi(null) } }
    }

    val showEmpty: LiveData<Boolean> = stores.map { it.isEmpty() }

    fun onFavouriteClicked(item: StoreListUi) {
        viewModelScope.launch {
            storeDao.setFavourite(item.id, !item.isFavourite)
        }
    }
}

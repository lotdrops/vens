package hackovid.vens.features.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao
import kotlinx.coroutines.launch

class FavouritesViewModel(private val storeDao: StoreDao) : ViewModel() {
    val stores = storeDao.getFavouritesByName()

    val showEmpty: LiveData<Boolean> = stores.map { it.isEmpty() }

    fun onFavouriteClicked(item: Store) {
        viewModelScope.launch {
            storeDao.setFavourite(item.id, !item.isFavourite)
        }
    }
}

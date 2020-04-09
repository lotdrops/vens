package hackovid.vens.features.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao
import kotlinx.coroutines.launch

class FavouritesViewModel(private val storeDao: StoreDao) : ViewModel() {
    val stores = storeDao.getFavouritesByName()

    fun onFavouriteClicked(item: Store) {
        viewModelScope.launch {
            storeDao.setFavourite(item.id, !item.isFavourite)
        }
    }
}

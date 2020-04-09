package hackovid.vens.features.favourites

import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.StoreDao

class FavouritesViewModel(storeDao: StoreDao) : ViewModel() {
    val stores = storeDao.getAllByName()
}

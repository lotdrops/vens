package hackovid.vens.features.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao
import kotlinx.coroutines.launch

class ListViewModel(private val storeDao: StoreDao) : ViewModel() {
    val stores = storeDao.getAllByName()

    fun onFavouriteClicked(item: Store) {
        viewModelScope.launch {
            storeDao.setFavourite(item.id, !item.isFavourite)
        }
    }
}

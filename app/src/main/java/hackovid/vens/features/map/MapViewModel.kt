package hackovid.vens.features.map

import androidx.lifecycle.*
import hackovid.vens.common.data.StoreDao
import kotlinx.coroutines.launch

class MapViewModel(private val storeDao: StoreDao) : ViewModel() {
    val stores = storeDao.getAllByName()

    val selectedStoreId = MutableLiveData<Int>()
    val selectedStore = selectedStoreId.switchMap { storeDao.getStoreById(it) }
    val showStoreInfo = selectedStoreId.map { it != null }

    fun onStoreActionClicked() {
    }

    fun onBackPressed(): Boolean = (showStoreInfo.value == true).apply {
        if (this) selectedStoreId.value = null
    }

    fun onFavouriteClicked() {
        viewModelScope.launch {
            selectedStore.value?.let { st -> storeDao.setFavourite(st.id, !st.isFavourite) }
        }
    }
}
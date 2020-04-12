package hackovid.vens.features.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.filter.StoresDataSource
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class MapViewModel(
    sharedViewModel: SharedViewModel,
    private val storesDataSource: StoresDataSource,
    private val storeDao: StoreDao
) : ViewModel() {
    val location = MutableLiveData<Location?>()
    private val queryParams = sharedViewModel.filter.combineWith(location) { filter, location ->
        filter?.let { Pair(it, location) }
    }
    val stores = queryParams.switchMap { storesDataSource.getData(it) }

    val selectedStoreId = MutableLiveData<Int>()
    val selectedStore = selectedStoreId.switchMap { storeDao.getStoreById(it) }
    val showStoreInfo = selectedStoreId.map { it != null }

    fun onBackPressed(): Boolean = (showStoreInfo.value == true).apply {
        if (this) selectedStoreId.value = null
    }

    fun onStoreActionClicked() {
        //TODO: navigate to detail
    }
    fun onFavouriteClicked() {
        viewModelScope.launch {
            selectedStore.value?.let { st -> storeDao.setFavourite(st.id, !st.isFavourite) }
        }
    }
}

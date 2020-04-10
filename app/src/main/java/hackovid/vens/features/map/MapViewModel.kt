package hackovid.vens.features.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.gms.maps.model.LatLng
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao

class MapViewModel(storeDao: StoreDao) : ViewModel() {
    val stores = storeDao.getAllByName()
    val location: MutableLiveData<LatLng?> = MutableLiveData(LatLng(DEFAULT_LAT, DEFAULT_LONG))

    val selectedStore = MutableLiveData<Store>()
    val showStoreInfo = selectedStore.map { it != null }

    fun onStoreActionClicked() {
    }

    fun onBackPressed(): Boolean = (showStoreInfo.value == true).apply {
        if (this) selectedStore.value = null
    }
}
private const val DEFAULT_LAT = 41.384771
private const val DEFAULT_LONG = 2.174065

package hackovid.vens.features.map

import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.StoreDao

class MapViewModel(storeDao: StoreDao) : ViewModel() {
    val stores = storeDao.getAllByName()
}

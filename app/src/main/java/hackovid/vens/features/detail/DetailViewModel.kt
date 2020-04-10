package hackovid.vens.features.detail

import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.StoreDao

class DetailViewModel(storeDao: StoreDao, storeId: Int) : ViewModel() {
    val store = storeDao.getStoreById(storeId)
}

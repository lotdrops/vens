package hackovid.vens.features.list

import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.StoreDao

class ListViewModel(storeDao: StoreDao) : ViewModel() {
    val stores = storeDao.getAllByName()
}

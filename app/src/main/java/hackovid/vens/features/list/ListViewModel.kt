package hackovid.vens.features.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.StoreDao

class ListViewModel(storeDao: StoreDao) : ViewModel() {
    var selectedStore : MutableLiveData<Int> = MutableLiveData()
    val stores = storeDao.getAllByName()

    fun onClickSelected() {
        selectedStore.value = 1
    }
}

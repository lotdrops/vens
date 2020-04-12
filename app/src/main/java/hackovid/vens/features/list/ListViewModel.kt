package hackovid.vens.features.list

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.filter.StoresDataSource
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.StoreListUi
import hackovid.vens.common.ui.toListUi
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class ListViewModel(
    sharedViewModel: SharedViewModel,
    private val storesDataSource: StoresDataSource,
    private val storeDao: StoreDao
) : ViewModel() {
    val location = sharedViewModel.location.map { latLang ->
        if (latLang == null) null
        else {
            Location("").apply {
                latitude = latLang.latitude
                longitude = latLang.longitude
            }
        }
    }
    private val queryParams = sharedViewModel.filter.combineWith(location) { filter, location ->
        filter?.let { Pair(it, location) }
    }
    val stores = queryParams.switchMap {
        storesDataSource.getData(it).map { it.map { store -> store.toListUi(location.value) } }
    }

    fun onFavouriteClicked(item: StoreListUi) {
        viewModelScope.launch {
            storeDao.setFavourite(item.id, !item.isFavourite)
        }
    }
}

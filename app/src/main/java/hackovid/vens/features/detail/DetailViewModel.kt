package hackovid.vens.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.toListUi
import hackovid.vens.common.ui.toLocation
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class DetailViewModel(
    sharedViewModel: SharedViewModel,
    private val storeDao: StoreDao,
    storeId: Int
) : ViewModel() {
    private val store = storeDao.getStoreById(storeId)
    val storeUi = sharedViewModel.location.combineWith(store) { latLng, store ->
        store?.toListUi(latLng.toLocation())
    }

    fun onFavouriteClicked() {
        viewModelScope.launch {
            val id = store.value?.id
            val isFavourite = store.value?.isFavourite
            if (id != null && isFavourite != null) {
                storeDao.setFavourite(id, !isFavourite)
            }
        }
    }
}

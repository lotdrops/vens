package hackovid.vens.features.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.data.filter.SortStrategy
import hackovid.vens.common.data.filter.StoresUseCase
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.toLocation
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class MapViewModel(
    private val sharedViewModel: SharedViewModel,
    private val storesUseCase: StoresUseCase,
    private val storeDao: StoreDao
) : ViewModel() {
    val navigateToDetail = SingleLiveEvent<Long>()

    val location = sharedViewModel.location.map { it.toLocation() }
    private val queryParams = sharedViewModel.filter.combineWith(location) { filter, location ->
        filter?.let { Pair(it, location) }
    }
    val stores = queryParams.switchMap {
        storesUseCase.getData(it.noSorting()).map {
                stores -> stores.map { store -> store.toClusterStoreItem(it?.second) } }
    }

    val selectedStoreId = MutableLiveData<Int>()
    val selectedStore = selectedStoreId.switchMap { id ->
        if (id != null) {
            storeDao.getStoreById(id).map { store ->
                store.toClusterStoreItem(sharedViewModel.location.value.toLocation())
            }
        } else MutableLiveData()
    }

    val showStoreInfo = selectedStoreId.map { it != null }

    private val cardMapPadding = MutableLiveData(0)
    val mapBottomPadding = cardMapPadding.combineWith(showStoreInfo) { padding, showing ->
        if (showing == true) padding ?: 0 else 0
    }

    fun setCardMapPadding(padding: Int) {
        cardMapPadding.value = padding
    }

    fun onBackPressed(): Boolean = (showStoreInfo.value == true).apply {
        if (this) selectedStoreId.value = null
    }

    fun onStoreActionClicked() {
        selectedStore.value?.id?.let { storeId ->
            navigateToDetail.value = storeId
        }
    }
    fun onFavouriteClicked() {
        viewModelScope.launch {
            selectedStore.value?.let { st -> storeDao.setFavourite(st.id, !st.isFavourite) }
        }
    }

    private fun Pair<FilterParams, Location?>?.noSorting() = if (this == null) null
    else Pair(this.first.copy(sortStrategy = SortStrategy.NONE), second)
}

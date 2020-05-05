package hackovid.vens.features.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import hackovid.vens.R
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.data.login.FirebaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.UiState
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class SelectStoreViewModel : ViewModel() {
    val query = MutableLiveData("")
    private val queryList = query.switchMap { query ->
        Log.d("asddd","query changed:$query")
        if (query.isBlank()) MutableLiveData(emptyList<Store>())
        else MutableLiveData(listOf(
            Store(id = 1, latitude = 0.0, longitude = 0.0, name = "Shop 1",
                type = StoreType.FASHION, subtype = StoreSubtype.BAZAAR, address = "address 1"),
            Store(id = 2, latitude = 0.0, longitude = 0.0, name = "Shop 2",
                type = StoreType.FASHION, subtype = StoreSubtype.BAZAAR, address = "address 2")
        )) // query list
    }
    val showEmptyQuery = query.map { it.isEmpty() }
    val showNoResults = queryList.combineWith(showEmptyQuery) { stores, emptyQuery ->
        emptyQuery == false && stores?.isEmpty() == true
    }
    private val selectedStoreId = MutableLiveData<Long?>()
    val selectedStore = selectedStoreId.combineWith(queryList) { id, stores ->
        if (id == null || stores == null) null
        else stores.first { it.id == id }
    }
    val showStoreInfo = selectedStore.map { it != null }
    val stores = queryList.combineWith(selectedStoreId) { stores, selectedId ->
        stores?.map { SelectableStore(it, it.id == selectedId) }
    }

    fun onStoreClicked(id: Long) {
        selectedStoreId.value = if (id == selectedStoreId.value) null else id
    }

    fun onClearSelection() {
        selectedStoreId.value = null
    }

    fun onSelectStoreClicked() {

    }

    fun onCreateStoreClicked() {

    }
}

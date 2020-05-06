package hackovid.vens.features.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import hackovid.vens.R
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.utils.combineWith

class SelectStoreViewModel : ViewModel() {
    val query = MutableLiveData("")
    private val queryList: LiveData<List<Store>> = query.switchMap { query ->
        val stores = (1..20).map { id ->
            Store(id = id.toLong(), latitude = 0.0, longitude = 0.0, name = "Shop $id",
                type = StoreType.FASHION, subtype = StoreSubtype.BAZAAR, address = "address $id")
        }
        val list = if (query.isBlank()) emptyList<Store>()
        else stores // query list
        val selectedStore = selectedStore.value
        if (selectedStore != null && !list.contains(selectedStore)) {
            MutableLiveData(list + selectedStore)
        } else {
            MutableLiveData(list)
        }
    }
    val showClearQuery = query.map { it.isNotEmpty() }
    val showNoResults = queryList.combineWith(query) { stores, query ->
        query?.isNotEmpty() == true && stores?.isEmpty() == true
    }
    val selectedStoreId = MutableLiveData<Long?>(null)
    val storeNotFoundSelected = selectedStoreId.map { it == NO_STORE_ID }
    val selectedStore = selectedStoreId.combineWith(queryList) { id, stores ->
        if (id == null || id <= 0 || stores == null) null
        else stores.first { it.id == id }
    }
    val stores = queryList.combineWith(selectedStoreId) { stores, selectedId ->
        stores?.map { SelectableStore(it, it.id == selectedId) }
    }
    val buttonText = storeNotFoundSelected.map { storeNotFoundSelected ->
        if (storeNotFoundSelected) R.string.select_store_button_create
        else R.string.select_store_button_select
    }
    val buttonEnabled = selectedStoreId.map { it != null }

    fun onStoreClicked(id: Long) {
        selectedStoreId.value = if (id == selectedStoreId.value) null else id
    }

    fun onClearQuery() {
        query.value = ""
    }

    fun onButtonClicked() {
    }
}
private const val NO_STORE_ID = -1L

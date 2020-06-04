package hackovid.vens.features.register
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import hackovid.vens.R
import hackovid.vens.common.data.filter.StoresUseCase
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.common.utils.combineWith

class SelectStoreViewModel(private val storesUseCase: StoresUseCase) : ViewModel() {
    val query = MutableLiveData("")

    val storeSelectedEvent = SingleLiveEvent<Unit>()

    private val queryList = query.switchMap { query ->
        if (query.isBlank()) MutableLiveData(emptyList())
        else storesUseCase.findStoreByName(query)
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

    val navBackEvent = SingleLiveEvent<Unit>()

    fun onBackClicked() {
        navBackEvent.call()
    }

    fun onStoreClicked(id: Long) {
        selectedStoreId.value = if (id == selectedStoreId.value) null else id
    }

    fun onClearQuery() {
        query.value = ""
    }

    fun onButtonClicked() {
        storeSelectedEvent.call()
    }
}
private const val NO_STORE_ID = -1L

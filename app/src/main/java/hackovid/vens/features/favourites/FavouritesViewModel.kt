package hackovid.vens.features.favourites

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.data.filter.SortStrategy
import hackovid.vens.common.data.filter.StoresUseCase
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.StoreListUi
import hackovid.vens.common.ui.toListUi
import hackovid.vens.common.ui.toLocation
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel(
    sharedViewModel: SharedViewModel,
    private val storesUseCase: StoresUseCase,
    private val storeDao: StoreDao
) : ViewModel() {
    private val location = sharedViewModel.location.map { it.toLocation() }
    private val queryParams = sharedViewModel.filter.combineWith(location) { filter, location ->
        filter?.let { Pair(it, location) }
    }
    val stores = queryParams.switchMap {
        storesUseCase.getData(it.byName()).map { stores ->
            stores.map { store -> store.toListUi(location.value) }
        }
    }

    val showEmpty: LiveData<Boolean> = stores.map { it.isEmpty() }

    init {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("coords","favslist getAllSZ:${storeDao.getAll().size}")
                val types = storeDao.prova().groupBy { it }.map { it.key }
                Log.d("coords","typeOfLong:$types")
            }
        }
    }

    fun onFavouriteClicked(item: StoreListUi) {
        viewModelScope.launch {
            storeDao.setFavourite(item.id, !item.isFavourite)
        }
    }

    private fun Pair<FilterParams, Location?>?.byName() = if (this == null) null
    else Pair(this.first.copy(sortStrategy = SortStrategy.NAME), second)
}

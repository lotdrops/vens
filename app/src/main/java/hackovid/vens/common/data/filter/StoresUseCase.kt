package hackovid.vens.common.data.filter

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.core.distance

/**
 * Since in SQL we cannot compute the exact distance, this class corrects the mistakes done
 * sorting the list
 */
class StoresUseCase(
    private val storesDataSource: StoresDataSource,
    private val emitTemporalSort: Boolean = true
) {
    fun getData(params: Pair<FilterParams, Location?>?): LiveData<List<Store>> = when {
        params == null -> MutableLiveData()
        params.first.sortStrategy == SortStrategy.NONE -> getStoresNoOrder(params)
        params.second != null && params.first.sortStrategy == SortStrategy.DISTANCE ->
            getStoresByDistance(params as Pair<FilterParams, Location>)
        else -> getStoresByName(params)
    }

    private fun getStoresByDistance(params: Pair<FilterParams, Location>): LiveData<List<Store>> {
        val res = MediatorLiveData<List<Store>>()
        res.addSource(storesDataSource.getDataByDistance(params)) { stores ->
            val filteredStores = stores.filterByDistance(params.first.distance, params.second)
            if (emitTemporalSort) {
                res.value = filteredStores.sortFirst(params.second)
            }
            res.value = filteredStores.sortAll(params.second)
        }
        return res
    }

    private fun List<Store>.filterByDistance(distance: Int, location: Location) = when (distance) {
        FilterParams.SHORT_DISTANCE -> filter { it.distance(location) <= SHORT_DISTANCE }
        FilterParams.MEDIUM_DISTANCE -> filter { it.distance(location) <= MEDIUM_DISTANCE }
        else -> this
    }

    private fun getStoresByName(params: Pair<FilterParams, Location?>?): LiveData<List<Store>> =
        storesDataSource.getDataByName(params)

    private fun getStoresNoOrder(params: Pair<FilterParams, Location?>?): LiveData<List<Store>> =
        storesDataSource.getData(params).map {
            val location = params?.second
            if (location != null) it.filterByDistance(params.first.distance, location)
            else it
        }

    private fun List<Store>.sortFirst(location: Location): List<Store> {
        val firstList = subList(0, FIRST_SORT_SIZE.coerceAtMost(size))
        val secondList = if (FIRST_SORT_SIZE < size) subList(FIRST_SORT_SIZE, size) else emptyList()
        return firstList.sortAll(location) + secondList
    }
    private fun List<Store>.sortAll(location: Location): List<Store> =
        sortedBy { it.distance(location) }

    private fun Store.distance(location: Location) = location.distance(latitude, longitude)
}
private const val FIRST_SORT_SIZE = 20
private const val SHORT_DISTANCE = 100
private const val MEDIUM_DISTANCE = 250

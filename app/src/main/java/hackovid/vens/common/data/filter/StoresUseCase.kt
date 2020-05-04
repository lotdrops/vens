package hackovid.vens.common.data.filter

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreAndFavourite
import hackovid.vens.common.data.core.distance

/**
 * Since in SQL we cannot compute the exact distance, this class corrects the mistakes done
 * sorting the list
 */
class StoresUseCase(
    private val storesDataSource: StoresDataSource,
    private val fastLoadFirstResults: Boolean = true
) {
    fun getData(params: Pair<FilterParams, Location?>?): LiveData<List<StoreAndFavourite>> = when {
        params == null -> MutableLiveData()
        params.first.sortStrategy == SortStrategy.NONE -> getStoresNoOrder(params)
        params.second != null && params.first.sortStrategy == SortStrategy.DISTANCE ->
            getStoresByDistance(params as Pair<FilterParams, Location>)
        else -> getStoresByName(params)
    }

    private fun getStoresNoOrder(params: Pair<FilterParams, Location?>?): LiveData<List<StoreAndFavourite>> =
        storesDataSource.getUnorderedData(params).map {
            val location = params?.second
            if (location != null) it.filterByDistance(params.first.distance, location)
            else it
        }

    private fun getStoresByDistance(params: Pair<FilterParams, Location>): LiveData<List<StoreAndFavourite>> {
        val res = MediatorLiveData<List<StoreAndFavourite>>()
        var sentFirstValue = false
        val emit: (List<StoreAndFavourite>) -> Unit = { stores ->
            sentFirstValue = true
            res.value = stores
                .filterByDistance(params.first.distance, params.second)
                .sortedBy { it.store.distance(params.second) }
        }
        val emitIfFirst: (List<StoreAndFavourite>) -> Unit = { stores ->
            if (!sentFirstValue) {
                sentFirstValue = true
                emit(stores)
            }
        }

        if (params.shouldFastLoadByDistance()) {
            res.addSource(
                storesDataSource.getDataByDistance(params, FIRST_RESULTS_SIZE),
                emitIfFirst
            )
        }
        res.addSource(storesDataSource.getDataByDistance(params), emit)

        return res
    }

    private fun getStoresByName(params: Pair<FilterParams, Location?>?): LiveData<List<StoreAndFavourite>> {
        val res = MediatorLiveData<List<StoreAndFavourite>>()
        var sentFirstValue = false
        val emit: (List<StoreAndFavourite>) -> Unit = { stores ->
            sentFirstValue = true
            res.value = stores.filtered(params)
        }
        val emitIfFirst: (List<StoreAndFavourite>) -> Unit = { if (!sentFirstValue) emit(it) }

        if (fastLoadFirstResults) {
            if (params != null)
                res.addSource(storesDataSource.getDataByName(params, FIRST_RESULTS_SIZE), emitIfFirst)
            else
                res.addSource(storesDataSource.getDataByName(FIRST_RESULTS_SIZE), emitIfFirst)
        }
        if (params != null)
            res.addSource(storesDataSource.getDataByName(params, -1), emit)
        else
            res.addSource(storesDataSource.getDataByName(-1), emit)

        return res
    }

    private fun Pair<FilterParams, Location>.shouldFastLoadByDistance() =
        fastLoadFirstResults && first.distance == FilterParams.ANY_DISTANCE

    private fun List<StoreAndFavourite>.filterByDistance(distance: Int, location: Location) = when (distance) {
        FilterParams.SHORT_DISTANCE -> filter { it.store.distance(location) <= SHORT_DISTANCE_METERS }
        FilterParams.MEDIUM_DISTANCE -> filter { it.store.distance(location) <= MEDIUM_DISTANCE_METERS }
        else -> this
    }

    private fun List<StoreAndFavourite>.filtered(params: Pair<FilterParams, Location?>?) =
        if (params?.second != null) {
            filterByDistance(params.first.distance, params.second!!)
        } else {
            this
        }

    private fun Store.distance(location: Location) = location.distance(latitude, longitude)
}

const val FIRST_RESULTS_SIZE = 20
const val SHORT_DISTANCE_METERS = 250
const val MEDIUM_DISTANCE_METERS = 500

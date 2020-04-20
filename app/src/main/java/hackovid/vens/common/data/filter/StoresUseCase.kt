package hackovid.vens.common.data.filter

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.core.distance

// TODO apply filter by distance. Must send to data a larger value to account for the error in
// TODO calculation, and fix it here
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
        params.second != null -> getStoresByDistance(params as Pair<FilterParams, Location>)
        else -> getStoresByName(params)
    }

    private fun getStoresByDistance(params: Pair<FilterParams, Location>): LiveData<List<Store>> {
        val res = MediatorLiveData<List<Store>>()
        res.addSource(storesDataSource.getDataByDistance(params)) { stores ->
            if (emitTemporalSort) {
                res.value = stores.sortFirst(params.second)
            }
            res.value = stores.sortAll(params.second)
        }
        return res
    }

    private fun getStoresByName(params: Pair<FilterParams, Location?>?): LiveData<List<Store>> =
        storesDataSource.getDataByName(params)

    private fun List<Store>.sortFirst(location: Location): List<Store> {
        val firstList = subList(0, FIRST_SORT_SIZE)
        val secondList = subList(FIRST_SORT_SIZE, size)
        return firstList.sortAll(location) + secondList
    }
    private fun List<Store>.sortAll(location: Location): List<Store> =
        sortedBy { location.distance(it.latitude, it.longitude) }
}
private const val FIRST_SORT_SIZE = 20

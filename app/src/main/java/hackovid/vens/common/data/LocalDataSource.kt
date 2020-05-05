package hackovid.vens.common.data

import hackovid.vens.common.data.filter.FilterParams

interface LocalDataSource<T> {

    fun readLocalStoreData(): List<T>?
}

interface LocalStorage {
    fun shouldBeDisplayedOnBoardScreen(): Boolean
    fun setOnboardScreenVisibility(shouldBeDisplayed: Boolean)
    fun isDataBaseAlreadyLoaded(): Boolean
    fun setDataBaseAlreadyLoaded(alreadyLoaded: Boolean)
    fun wasLocationPermissionRequested(): Boolean
    fun setLocationPermissionRequested()
    fun setFilterParams(params: FilterParams)
    fun getFilterParams(): FilterParams
    fun getLastUpdateTimestamp(): Long
    fun setLastUpdateTimestamp(timestamp: Long)
}

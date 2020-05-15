package hackovid.vens.common.data

import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.data.login.User

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
    fun getLastUpdateTimestamp(): Long
    fun setLastUpdateTimestamp(timestamp: Long)
    fun getTosAcceptedVersion(): Int
    fun setTosAcceptedVersion(version: Int)
    fun setFilterParams(params: FilterParams)
    fun getFilterParams(): FilterParams
    fun setListIsFavourites(listIsFavourites: Boolean)
    fun getListIsFavourites(): Boolean
    fun getUserDataOnRegister(): User?
    fun setUserDataOnRegister(user: User)
}

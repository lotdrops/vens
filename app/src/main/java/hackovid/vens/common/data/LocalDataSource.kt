package hackovid.vens.common.data

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
}

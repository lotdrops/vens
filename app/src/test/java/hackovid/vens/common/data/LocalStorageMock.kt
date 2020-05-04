package hackovid.vens.common.data

class LocalStorageMock : LocalStorage {
    private var showOnboarding = false
    private var dbLoaded = false
    private var locationRequested = false
    private var _lastUpdateTimestamp = 0L

    override fun shouldBeDisplayedOnBoardScreen() = showOnboarding

    override fun setOnboardScreenVisibility(shouldBeDisplayed: Boolean) {
        showOnboarding = shouldBeDisplayed
    }

    override fun isDataBaseAlreadyLoaded() = dbLoaded

    override fun setDataBaseAlreadyLoaded(alreadyLoaded: Boolean) {
        dbLoaded = alreadyLoaded
    }

    override fun wasLocationPermissionRequested() = locationRequested

    override fun setLocationPermissionRequested() {
        locationRequested = true
    }

    override fun getLastUpdateTimestamp() = _lastUpdateTimestamp

    override fun setLastUpdateTimestamp(timestamp: Long) {
        _lastUpdateTimestamp = timestamp
    }
}

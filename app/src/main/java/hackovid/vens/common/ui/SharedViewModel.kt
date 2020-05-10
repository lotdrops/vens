package hackovid.vens.common.ui

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.config.FirebaseRemoteConfigController
import hackovid.vens.common.data.config.RemoteConfig
import hackovid.vens.common.data.updatedata.UpdateDataUseCase
import hackovid.vens.common.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

class SharedViewModel(
    localStorage: LocalStorage,
    updateDataUseCase: UpdateDataUseCase,
    firebaseRemoteConfigController: FirebaseRemoteConfigController,
    remoteConfig: RemoteConfig
) : ViewModel() {
    private val _location: MutableLiveData<LatLng?> = MutableLiveData()
    val location: LiveData<LatLng?> get() = _location

    val requestLocationEvent = SingleLiveEvent<Unit>()

    val userRequestsLocationEvent = SingleLiveEvent<Unit>()
    val onLocationAccepted = SingleLiveEvent<Unit>()

    val filter = MutableLiveData(localStorage.getFilterParams())

    val minForcedVersion = remoteConfig.minForcedVersion
    val updateStoreUrl = "" //TODO add real url playstore

    val onObserverActive: LiveData<Unit> = object : LiveData<Unit>(Unit) {
        override fun onActive() {
            firebaseRemoteConfigController.fetchAndActivate()
        }
    }

    init {
        if (!localStorage.wasLocationPermissionRequested()) {
            requestLocationEvent.call()
            localStorage.setLocationPermissionRequested()
        }
        viewModelScope.launch(NonCancellable + Dispatchers.Default) {
            updateDataUseCase.updateData()
        }
    }

    fun onNewLocation(newLocation: LatLng?) {
        val currentLocation = location.value
        if (currentLocation != newLocation && !(currentLocation != null && newLocation != null &&
                    currentLocation.toLocation()!!.distanceTo(newLocation.toLocation()) <
                    MIN_DISTANCE_UPDATE)) {
            _location.value = newLocation
        }
    }
}
private const val MIN_DISTANCE_UPDATE = 20

fun LatLng?.toLocation() = if (this == null) null
else {
    Location("").apply {
        latitude = this@toLocation.latitude
        longitude = this@toLocation.longitude
    }
}

const val DEFAULT_LAT = 41.384771
const val DEFAULT_LONG = 2.174065

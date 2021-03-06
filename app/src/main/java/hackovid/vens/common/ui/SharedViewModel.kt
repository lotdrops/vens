package hackovid.vens.common.ui

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import hackovid.vens.common.data.filter.FilterParams

class SharedViewModel : ViewModel() {
    val location: MutableLiveData<LatLng?> = MutableLiveData()

    val filter = MutableLiveData(
        FilterParams(
            FilterParams.defaultCategories()
        )
    )
}

fun LatLng?.toLocation() = if (this == null) null
else {
    Location("").apply {
        latitude = this@toLocation.latitude
        longitude = this@toLocation.longitude
    }
}

const val DEFAULT_LAT = 41.384771
const val DEFAULT_LONG = 2.174065

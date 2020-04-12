package hackovid.vens.common.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import hackovid.vens.common.data.filter.FilterParams

class SharedViewModel : ViewModel() {
    val location: MutableLiveData<LatLng?> = MutableLiveData(LatLng(DEFAULT_LAT, DEFAULT_LONG))

    val filter = MutableLiveData(
        FilterParams(
            FilterParams.defaultCategories()
        )
    )
}

private const val DEFAULT_LAT = 41.384771
private const val DEFAULT_LONG = 2.174065

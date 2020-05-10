package hackovid.vens.features.onboarding.di

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class OnBoardingSharedViewModel : ViewModel() {
    var onMapResult: LatLng? = null
}

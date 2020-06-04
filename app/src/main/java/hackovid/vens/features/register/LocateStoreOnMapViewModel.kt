package hackovid.vens.features.register
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.common.utils.SingleLiveEvent

class LocateStoreOnMapViewModel() : ViewModel() {
    val enableButton: MutableLiveData<Boolean> = MutableLiveData()

    val navBackEvent = SingleLiveEvent<Unit>()

    fun onBackClicked() {
        navBackEvent.call()
    }
}

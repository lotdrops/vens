package hackovid.vens.features.login

import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.login.FirebaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.utils.SingleLiveEvent

class GdprViewModel : ViewModel() {

    val acceptEvent = SingleLiveEvent<Unit>()

    fun onAcceptClicked() {
        acceptEvent.call()
    }
}

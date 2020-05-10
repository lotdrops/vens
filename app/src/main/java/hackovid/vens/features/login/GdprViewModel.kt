package hackovid.vens.features.login

import androidx.lifecycle.ViewModel
import hackovid.vens.BuildConfig
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.utils.SingleLiveEvent

class GdprViewModel(private val localStorage: LocalStorage) : ViewModel() {

    val acceptEvent = SingleLiveEvent<Unit>()

    fun onAcceptClicked() {
        setAcceptedTos()
        acceptEvent.call()
    }

    private fun setAcceptedTos() {
        localStorage.setTosAcceptedVersion(BuildConfig.TOS_VERSION)
    }
}

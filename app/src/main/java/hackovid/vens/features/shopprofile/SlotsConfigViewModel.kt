package hackovid.vens.features.shopprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import hackovid.vens.common.utils.SingleLiveEvent

class SlotsConfigViewModel : ViewModel() {
    val scheduleEvent = SingleLiveEvent<Unit>()

    val noteEnabled = MutableLiveData(false)
    val requirePhoneEnabled = MutableLiveData(false)
    val publishStatusEnabled = MutableLiveData(false)

    private val scheduleSet = MutableLiveData(false)
    private val slotsEnabled = MutableLiveData(false)
    val showEnableButton = slotsEnabled.map { !it }
    val showScheduleButton = scheduleSet.map { !it }
    val showDisableButton: LiveData<Boolean> get() = slotsEnabled
    val shopUpdateButton: LiveData<Boolean> get() = slotsEnabled

    fun onEnableClicked() {

    }

    fun onDisableClicked() {

    }

    fun onUpdateClicked() {

    }

    fun onScheduleClicked() {
        scheduleEvent.call()
    }
}
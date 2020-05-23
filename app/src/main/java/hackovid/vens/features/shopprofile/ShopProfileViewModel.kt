package hackovid.vens.features.shopprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.utils.SingleLiveEvent

class ShopProfileViewModel : ViewModel() {
    val enableSlotsEvent = SingleLiveEvent<Unit>()
    val pendingSlotsEvent = SingleLiveEvent<Unit>()
    val scheduleEvent = SingleLiveEvent<Unit>()

    val slotsEnabled = MutableLiveData(false)
    val pendingRequests = MutableLiveData(0)

    val store = MutableLiveData(Store(0, 0.0, 0.0, "Shop name",
        StoreType.FASHION, StoreSubtype.APPLIANCES, "987654321","612345789",
        "Madeup address, 1","www.shop.com", "contact@shop.com","",true, false
    ))

    fun onEnableSlotsClicked() {
        enableSlotsEvent.call()
    }

    fun onPendingSlotsClicked() {
        pendingSlotsEvent.call()
    }

    fun onScheduleClicked() {
        scheduleEvent.call()
    }
}
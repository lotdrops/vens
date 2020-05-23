package hackovid.vens.features.slots

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class SlotsViewModel : ViewModel() {
    val slotsOfToday = MutableLiveData(emptyList<String>())
    val showNoSlots = slotsOfToday.map { it.isEmpty() }


}
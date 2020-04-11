package hackovid.vens.common.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.filter.FilterParams

class SharedViewModel : ViewModel() {
    val filter = MutableLiveData(
        FilterParams(
            FilterParams.defaultCategories()
        )
    )
}

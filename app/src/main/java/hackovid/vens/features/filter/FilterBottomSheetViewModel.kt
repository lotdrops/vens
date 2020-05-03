package hackovid.vens.features.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.data.filter.MEDIUM_DISTANCE_METERS
import hackovid.vens.common.data.filter.SHORT_DISTANCE_METERS
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.utils.SingleLiveEvent

class FilterBottomSheetViewModel(
    private val sharedViewModel: SharedViewModel,
    private val localStorage: LocalStorage
) : ViewModel() {
    val categories = MutableLiveData<List<Boolean>>()
    val priorities = MutableLiveData(FilterParams.defaultSelectable())
    val distance = MutableLiveData<Int?>()
    val mustHave = MutableLiveData(FilterParams.defaultSelectable())
    val crowd = MutableLiveData<Int?>()

    val closeFilter = SingleLiveEvent<Unit>()

    val shortDistance = SHORT_DISTANCE_METERS
    val mediumDistance = MEDIUM_DISTANCE_METERS

    init {
        val initialFilterConfig = localStorage.getFilterParams()
        categories.value = initialFilterConfig.categories
        distance.value = initialFilterConfig.distance
        crowd.value = initialFilterConfig.crowd
    }

    fun onCategorySelected(position: Int, isSelected: Boolean) {
        categories.value = (categories.value ?: FilterParams.defaultCategories())
            .setInPosition(isSelected, position)
    }

    fun onPrioritySelected(position: Int) {
        priorities.value = (priorities.value ?: FilterParams.defaultSelectable())
            .flipPosition(position)
    }

    fun onDistanceSelected(position: Int) {
        if (distance.value == position) distance.value = null
        else distance.value = position
    }

    fun onMustHaveSelected(position: Int) {
        mustHave.value = (mustHave.value ?: FilterParams.defaultSelectable()).flipPosition(position)
    }

    fun onCrowdSelected(position: Int) {
        if (crowd.value == position) crowd.value = null
        else crowd.value = position
    }

    fun onFilterClicked() {
        val categories = categories.value ?: FilterParams.defaultCategories()
        val distance = distance.value ?: 0
        val crowd = crowd.value ?: 0
        val params = FilterParams(
            categories,
            distance,
            crowd
        )
        sharedViewModel.filter.value = params
        localStorage.setFilterParams(params)
        closeFilter.call()
    }

    fun onCloseClicked() {
        closeFilter.call()
    }

    private fun List<Boolean>.setInPosition(value: Boolean, position: Int) =
        this.toMutableList().apply { set(position, value) }.toList()

    private fun List<Boolean>.flipPosition(position: Int) =
        this.toMutableList().apply { set(position, !this[position]) }.toList()
}

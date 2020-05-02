package hackovid.vens.features.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.data.filter.MEDIUM_DISTANCE_METERS
import hackovid.vens.common.data.filter.SHORT_DISTANCE_METERS
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.utils.SingleLiveEvent

class FilterBottomSheetViewModel(private val sharedViewModel: SharedViewModel) : ViewModel() {
    val categories = MutableLiveData<List<Boolean>>(
        sharedViewModel.filter.value?.categories ?: FilterParams.defaultCategories()
    )
    val priorities = MutableLiveData(FilterParams.defaultSelectable())
    val distance = MutableLiveData<Int?>(
        sharedViewModel.filter.value?.distance ?: FilterParams.DEFAULT_DISTANCE
    )
    val mustHave = MutableLiveData(FilterParams.defaultSelectable())
    val crowd = MutableLiveData<Int?>(
        sharedViewModel.filter.value?.crowd ?: FilterParams.DEFAULT_CROWD
    )

    val closeFilter = SingleLiveEvent<Unit>()

    val shortDistance = SHORT_DISTANCE_METERS
    val mediumDistance = MEDIUM_DISTANCE_METERS

    fun onCategorySelected(position: Int, isSelected: Boolean) {
        val initialValue = categories.value ?: FilterParams.defaultCategories()
        categories.value = initialValue
            .takeIf { it.canSwitchCategory(isSelected) }
            ?.setInPosition(isSelected, position)
            ?: initialValue
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
        closeFilter.call()
    }

    fun onCloseClicked() {
        closeFilter.call()
    }

    private fun List<Boolean>.setInPosition(value: Boolean, position: Int) =
        this.toMutableList().apply { set(position, value) }.toList()

    private fun List<Boolean>.flipPosition(position: Int) =
        this.toMutableList().apply { set(position, !this[position]) }.toList()

    private fun List<Boolean>.canSwitchCategory(newValue: Boolean) =
        newValue || filter { it }.count() > 1
}

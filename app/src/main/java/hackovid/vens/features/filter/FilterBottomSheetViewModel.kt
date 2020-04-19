package hackovid.vens.features.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.utils.SingleLiveEvent

class FilterBottomSheetViewModel(private val sharedViewModel: SharedViewModel) : ViewModel() {
    val categories = MutableLiveData(
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

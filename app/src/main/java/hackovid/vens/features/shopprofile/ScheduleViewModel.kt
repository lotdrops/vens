package hackovid.vens.features.shopprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import hackovid.vens.R
import hackovid.vens.common.utils.ResourceString
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.common.utils.UiText
import hackovid.vens.common.utils.UiTextOrResource
import hackovid.vens.common.utils.combineLiveDatas
import hackovid.vens.common.utils.toUiText
import kotlin.math.max
import kotlin.math.min

class ScheduleViewModel : ViewModel() {
    val selectTimesEvent = SingleLiveEvent<Unit>()
    val rangeOverlapsError = SingleLiveEvent<Unit>()
    val rangeTooShortError = SingleLiveEvent<Unit>()

    private val schedule = MutableLiveData(ShopSchedule())
    val mondaySchedule = schedule.map { it.scheduleForDay(WeekDay.Monday) }
    val tuesdaySchedule = schedule.map { it.scheduleForDay(WeekDay.Tuesday) }
    val wednesdaySchedule = schedule.map { it.scheduleForDay(WeekDay.Wednesday) }
    val thursdaySchedule = schedule.map { it.scheduleForDay(WeekDay.Thursday) }
    val fridaySchedule = schedule.map { it.scheduleForDay(WeekDay.Friday) }
    val saturdaySchedule = schedule.map { it.scheduleForDay(WeekDay.Saturday) }
    val sundaySchedule = schedule.map { it.scheduleForDay(WeekDay.Sunday) }
    val holidayEveSchedule = schedule.map { it.scheduleForDay(WeekDay.HolidayEve) }
    val holidaySchedule = schedule.map { it.scheduleForDay(WeekDay.Holiday) }

    private val selectedWeekDays = MutableLiveData<List<WeekDay>>(emptyList())
    private val _currentTimeRanges = MutableLiveData(emptyList<TimeRange>())
    val shopEmptyRangesInfo = _currentTimeRanges.map { it.isEmpty() }
    val currentTimeRanges = _currentTimeRanges.map { ranges -> ranges.map { it.toText() } }

    val timeRangeButtonEnabled = selectedWeekDays.map { it.isNotEmpty() }
    val doneButtonEnabled = selectedWeekDays.map { it.isNotEmpty() }

    val showMonday = schedule.map { !it.hasDay(WeekDay.Monday) }
    val showTuesday = schedule.map { !it.hasDay(WeekDay.Tuesday) }
    val showWednesday = schedule.map { !it.hasDay(WeekDay.Wednesday) }
    val showThursday = schedule.map { !it.hasDay(WeekDay.Thursday) }
    val showFriday = schedule.map { !it.hasDay(WeekDay.Friday) }
    val showSaturday = schedule.map { !it.hasDay(WeekDay.Saturday) }
    val showSunday = schedule.map { !it.hasDay(WeekDay.Sunday) }
    val showHolidayEve = schedule.map { !it.hasDay(WeekDay.HolidayEve) }
    val showHoliday = schedule.map { !it.hasDay(WeekDay.Holiday) }
    val mondayIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.Monday) }
    val tuesdayIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.Tuesday) }
    val wednesdayIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.Wednesday) }
    val thursdayIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.Thursday) }
    val fridayIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.Friday) }
    val saturdayIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.Saturday) }
    val sundayIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.Sunday) }
    val holidayEveIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.HolidayEve) }
    val holidayIsSelected = selectedWeekDays.map { weekDayIsSelected(WeekDay.Holiday) }

    val isEditing = combineLiveDatas(showMonday, showTuesday, showWednesday, showThursday,
        showFriday, showSaturday, showSunday, showHolidayEve, showHoliday) {
        showMonday.value != false || showTuesday.value != false || showWednesday.value != false ||
                showThursday.value != false || showFriday.value != false ||
                showSaturday.value != false || showSunday.value != false ||
                showHolidayEve.value != false || showHoliday.value != false
    }

    private var selectedTime: Int? = null

    fun onAddRangeClicked() {
        selectedTime = null
        selectTimesEvent.call()
    }

    fun onTimeSelected(hour: Int, min: Int) {
        val firstTime = selectedTime
        if (firstTime == null) {
            selectedTime = hour * 60 + min
            selectTimesEvent.call()
        } else {
            val secondTime = if (hour == 0 && min == 0) 24 * 60 else hour * 60 + min
            val newRange = TimeRange(min(firstTime, secondTime), max(firstTime, secondTime))
            when {
                newRange.isTooShort() -> {
                    rangeTooShortError.call()
                }
                timeRangeOverlaps(newRange) -> {
                    rangeOverlapsError.call()
                }
                else -> {
                    val currentRange = _currentTimeRanges.value ?: emptyList()
                    _currentTimeRanges.value = (currentRange + newRange).sortedBy { it.start }
                }
            }
            selectedTime = null
        }
    }

    private fun TimeRange.isTooShort() =
        end - start < MIN_TIME_RANGE_DURATION

    private fun timeRangeOverlaps(newTimeRange: TimeRange) =
        _currentTimeRanges.value?.any { timeRange ->
            !(newTimeRange.start > timeRange.end || newTimeRange.end < timeRange.start)
        } != false

    fun onWeekDayClicked(weekDay: WeekDay) {
        val selected = selectedWeekDays.value ?: emptyList()
        selectedWeekDays.value = if (weekDayIsSelected(weekDay)) {
            selected.filter { it != weekDay }
        } else {
            selected + weekDay
        }
    }

    fun onDoneClicked() {
        val groupOfDays = schedule.value?.groupOfDays ?: emptyList()
        val timeRanges = _currentTimeRanges.value ?: emptyList()
        val selectedWeekDays = selectedWeekDays.value ?: emptyList()
        val newDayShopSchedule = DaysShopSchedule(selectedWeekDays, timeRanges)
        schedule.value = ShopSchedule(groupOfDays + newDayShopSchedule)
        _currentTimeRanges.value = emptyList()
        this.selectedWeekDays.value = emptyList()
    }

    fun onRemoveTimeRangeClicked(item: String) {
        val position = currentTimeRanges.value?.indexOf(item) ?: -1
        _currentTimeRanges.value = _currentTimeRanges.value
            ?.filterIndexed { index, _ -> index != position }
    }

    fun onEditScheduleDay(weekDay: WeekDay) {
        schedule.value = schedule.value?.removeWeekDay(weekDay)
    }

    private fun ShopSchedule.removeWeekDay(weekDay: WeekDay) = ShopSchedule(
        groupOfDays
            .map { DaysShopSchedule(it.days.filter { day -> day != weekDay }, it.timeRanges) }
            .filter { it.days.isNotEmpty() }
    )

    private fun weekDayIsSelected(weekDay: WeekDay) =
        selectedWeekDays.value?.contains(weekDay) == true
}

private fun ShopSchedule.hasDay(day: WeekDay) = groupOfDays
    .flatMap { it.days }
    .any { it == day }

private fun TimeRange.toText() =
    "${start.minToHourMin()} - ${end.minToHourMin()}"
private fun Int.minToHourMin() =
    "${(this / 60).addStartZeroIfNeeded()}:${rem(60).addStartZeroIfNeeded()}"
private fun Int.addStartZeroIfNeeded() = if (this < 10) "0${this}" else "$this"

private fun ShopSchedule.scheduleForDay(day: WeekDay): UiTextOrResource = groupOfDays
    .firstOrNull { daysShopSchedule -> daysShopSchedule.days.contains(day) }
    ?.timeRanges?.map { timeRange -> timeRange.toText() }
    ?.joinToString("\n") { it }
    ?.toUiText()
    ?.closedResStringIfEmpty()
    ?: ResourceString(R.string.shop_schedule_not_set)

private fun UiText.closedResStringIfEmpty() =
    if (text.isEmpty()) ResourceString(R.string.shop_schedule_closed)
    else this

const val MIN_TIME_RANGE_DURATION = 15
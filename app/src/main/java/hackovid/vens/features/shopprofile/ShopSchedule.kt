package hackovid.vens.features.shopprofile

data class ShopSchedule(val groupOfDays: List<DaysShopSchedule> = emptyList())

data class DaysShopSchedule(
    val days: List<WeekDay>,
    val timeRanges: List<TimeRange>
)

data class TimeRange(val start: Int, val end: Int)

enum class WeekDay {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday,
    HolidayEve,
    Holiday
}
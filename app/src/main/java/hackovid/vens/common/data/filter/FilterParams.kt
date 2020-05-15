package hackovid.vens.common.data.filter

import hackovid.vens.common.data.StoreType

data class FilterParams(
    val categories: List<Boolean>,
    val distance: Int = DEFAULT_DISTANCE,
    val crowd: Int = DEFAULT_CROWD,
    val sortStrategy: SortStrategy = SortStrategy.DISTANCE,
    val favouritesOnly: Boolean = false
) {
    companion object {
        const val DEFAULT_DISTANCE = 0
        const val SHORT_DISTANCE = 0
        const val MEDIUM_DISTANCE = 1
        const val ANY_DISTANCE = 2
        const val DEFAULT_CROWD = 0
        fun defaultCategories() =
            StoreType.values().map { it == StoreType.FOOD || it == StoreType.MARKET }
        fun defaultSelectable() =
            listOfFalse(NUMBER_OF_SELECTABLE)

        fun createWithoutFiltering() = FilterParams(
            categories = listOfTrue(NUMBER_OF_SELECTABLE),
            distance = ANY_DISTANCE
        )
    }
}
private const val NUMBER_OF_SELECTABLE = 3
private fun listOfFalse(size: Int): List<Boolean> = arrayOfNulls<Unit>(size).map { false }
private fun listOfTrue(size: Int): List<Boolean> = arrayOfNulls<Unit>(size).map { true }

enum class SortStrategy { NAME, DISTANCE, NONE }

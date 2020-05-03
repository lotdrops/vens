package hackovid.vens.common.data.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.filter.FilterParams
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class SharedPreferencesPersistence(private val preferences: SharedPreferences) : LocalStorage {
    override fun shouldBeDisplayedOnBoardScreen() = preferences
        .getBoolean(ON_BOARDING_SHOULD_BE_DISPLAYED, true)

    override fun setOnboardScreenVisibility(shouldBeDisplayed: Boolean) = preferences
        .edit { putBoolean(ON_BOARDING_SHOULD_BE_DISPLAYED, shouldBeDisplayed) }

    override fun isDataBaseAlreadyLoaded() = preferences
        .getBoolean(DATABASE_LOADED_IDENTIFIER, false)

    override fun setDataBaseAlreadyLoaded(alreadyLoaded: Boolean) = preferences
        .edit { putBoolean(DATABASE_LOADED_IDENTIFIER, alreadyLoaded) }

    override fun wasLocationPermissionRequested() = preferences
        .getBoolean(LOCATION_PERMISSION_REQUESTED, false)

    override fun setLocationPermissionRequested() = preferences
        .edit { putBoolean(LOCATION_PERMISSION_REQUESTED, true) }

    override fun getFilterParams() = FilterParams(
        categories = deserializeCategories(preferences.getString(FILTER_PARAMS_CATEGORIES, null)),
        distance = preferences.getInt(FILTER_PARAMS_DISTANCE, FilterParams.DEFAULT_DISTANCE)
    )

    override fun setFilterParams(params: FilterParams) = preferences.run {
        edit { putString(FILTER_PARAMS_CATEGORIES, params.categories.serialize()) }
        edit { putInt(FILTER_PARAMS_DISTANCE, params.distance) }
    }

    private fun List<Boolean>.serialize() =
        Json(JsonConfiguration.Stable).stringify(Boolean.serializer().list, this)

    private fun deserializeCategories(serial: String?): List<Boolean> =
        if (serial == null) FilterParams.defaultCategories() else try {
            Json(JsonConfiguration.Stable).parse(Boolean.serializer().list, serial)
        } catch (e: Exception) {
            FilterParams.defaultCategories()
        }
}
private const val ON_BOARDING_SHOULD_BE_DISPLAYED = "on_boarding_should_be_displayed"
private const val DATABASE_LOADED_IDENTIFIER = "database_loaded_identifier"
private const val LOCATION_PERMISSION_REQUESTED = "location_permission_requested"
private const val FILTER_PARAMS_CATEGORIES = "filter_params_categories"
private const val FILTER_PARAMS_DISTANCE = "filter_params_distance"

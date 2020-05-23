package hackovid.vens.common.data.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.filter.FilterParams
import hackovid.vens.common.data.login.User
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

    override fun getListIsFavourites() = preferences.getBoolean(LIST_IS_FAVOURITES, false)
    override fun getUserDataOnRegister(): User? {
        val stringUser = preferences.getString(USER_DATA_ON_REGISTER, "")
        return if (stringUser.isNullOrEmpty()) null
        else Json(JsonConfiguration.Stable).parse(User.serializer(), stringUser)
    }

    override fun setUserDataOnRegister(user: User?) {
        if (user != null) {
            val stringUser = Json(JsonConfiguration.Stable).stringify(User.serializer(), user)
            preferences.edit { putString(USER_DATA_ON_REGISTER, stringUser) }
        } else {
            preferences.edit { putString(USER_DATA_ON_REGISTER, "") }
        }
    }

    override fun setListIsFavourites(listIsFavourites: Boolean) = preferences
        .edit { putBoolean(LIST_IS_FAVOURITES, listIsFavourites) }

    override fun getLastUpdateTimestamp() = preferences
            .getLong(LAST_UPDATE_TIMESTAMP, 0)

    override fun setLastUpdateTimestamp(timestamp: Long) = preferences.run {
        edit { putLong(LAST_UPDATE_TIMESTAMP, timestamp) }
    }

    override fun getTosAcceptedVersion() = preferences.getInt(TOS_VERSION, -1)
    override fun setTosAcceptedVersion(version: Int) = preferences
        .edit { putInt(TOS_VERSION, version) }

    private fun List<Boolean>.serialize() =
        Json(JsonConfiguration.Stable).stringify(Boolean.serializer().list, this)

    private fun deserializeCategories(serial: String?): List<Boolean> =
        if (serial == null) FilterParams.defaultCategories() else try {
            Json(JsonConfiguration.Stable).parse(Boolean.serializer().list, serial)
        } catch (e: Exception) {
            FilterParams.defaultCategories()
        }

    override fun isFirstLogin() = preferences.getBoolean(FIRST_TIME_LOGIN, true)

    override fun setFirstLogin(firstTime: Boolean) { preferences
        .edit { putBoolean(FIRST_TIME_LOGIN, firstTime) }
    }
}

private const val ON_BOARDING_SHOULD_BE_DISPLAYED = "on_boarding_should_be_displayed"
private const val DATABASE_LOADED_IDENTIFIER = "database_loaded_identifier"
private const val LOCATION_PERMISSION_REQUESTED = "location_permission_requested"
private const val LAST_UPDATE_TIMESTAMP = "last_update_timestamp"
private const val TOS_VERSION = "tos_version"
private const val FILTER_PARAMS_CATEGORIES = "filter_params_categories"
private const val FILTER_PARAMS_DISTANCE = "filter_params_distance"
private const val LIST_IS_FAVOURITES = "list_as_favourites"
private const val USER_DATA_ON_REGISTER = "user_data_on_register"
private const val FIRST_TIME_LOGIN = "first_time_login"

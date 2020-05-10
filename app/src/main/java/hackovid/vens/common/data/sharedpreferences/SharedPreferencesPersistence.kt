package hackovid.vens.common.data.sharedpreferences

import android.content.Context
import hackovid.vens.common.data.LocalStorage

class SharedPreferencesPersistence(val context: Context) : LocalStorage {

    override fun shouldBeDisplayedOnBoardScreen() =
        context.getSharedPreferences(ON_BOARDING_SHOULD_BE_DISPLAYED, Context.MODE_PRIVATE)
            .getBoolean(ON_BOARDING_SHOULD_BE_DISPLAYED, true)

    override fun setOnboardScreenVisibility(shouldBeDisplayed: Boolean) =
        context.getSharedPreferences(ON_BOARDING_SHOULD_BE_DISPLAYED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(ON_BOARDING_SHOULD_BE_DISPLAYED, shouldBeDisplayed)
            .apply()

    override fun isDataBaseAlreadyLoaded() =
        context.getSharedPreferences(DATABASE_LOADED_IDENTIFIER, Context.MODE_PRIVATE)
            .getBoolean(DATABASE_LOADED_IDENTIFIER, false)

    override fun setDataBaseAlreadyLoaded(alreadyLoaded: Boolean) =
        context.getSharedPreferences(DATABASE_LOADED_IDENTIFIER, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(DATABASE_LOADED_IDENTIFIER, alreadyLoaded)
            .apply()

    override fun wasLocationPermissionRequested() =
        context.getSharedPreferences(LOCATION_PERMISSION_REQUESTED, Context.MODE_PRIVATE)
            .getBoolean(LOCATION_PERMISSION_REQUESTED, false)

    override fun setLocationPermissionRequested() =
        context.getSharedPreferences(LOCATION_PERMISSION_REQUESTED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(LOCATION_PERMISSION_REQUESTED, true)
            .apply()

    override fun getLastUpdateTimestamp() =
        context.getSharedPreferences(LAST_UPDATE_TIMESTAMP, Context.MODE_PRIVATE)
            .getLong(LAST_UPDATE_TIMESTAMP, 0)

    override fun setLastUpdateTimestamp(timestamp: Long) =
        context.getSharedPreferences(LAST_UPDATE_TIMESTAMP, Context.MODE_PRIVATE)
            .edit()
            .putLong(LAST_UPDATE_TIMESTAMP, timestamp)
            .apply()

    override fun getTosAcceptedVersion() =
        context.getSharedPreferences(TOS_VERSION, Context.MODE_PRIVATE).getInt(TOS_VERSION, -1)

    override fun setTosAcceptedVersion(version: Int) =
        context.getSharedPreferences(TOS_VERSION, Context.MODE_PRIVATE)
            .edit()
            .putInt(TOS_VERSION, version)
            .apply()
}
private const val ON_BOARDING_SHOULD_BE_DISPLAYED = "on_boarding_should_be_displayed"
private const val DATABASE_LOADED_IDENTIFIER = "database_loaded_identifier"
private const val LOCATION_PERMISSION_REQUESTED = "location_permission_requested"
private const val LAST_UPDATE_TIMESTAMP = "last_update_timestamp"
private const val TOS_VERSION = "tos_version"

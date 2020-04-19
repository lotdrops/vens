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
}
private const val ON_BOARDING_SHOULD_BE_DISPLAYED = "on_boarding_should_be_displayed"
private const val DATABASE_LOADED_IDENTIFIER = "database_loaded_identifier"

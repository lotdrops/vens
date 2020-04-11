package hackovid.vens.common.data.sharedpreferences

import android.content.Context
import hackovid.vens.common.data.LocalStorage

class SharedPreferencesPersitency(val context: Context): LocalStorage {

    private val ON_BOARDING_SHOULD_BE_DISPLAYED = "on_boarding_should_be_displayed"


    override fun shouldBeDisplayedOnBoardScreen(): Boolean {
        val sharedPreferences = context.getSharedPreferences(ON_BOARDING_SHOULD_BE_DISPLAYED, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(ON_BOARDING_SHOULD_BE_DISPLAYED, true)

    }

    override fun setOnboardScreenVisibility(shouldBeDisplayed: Boolean) {
        val sharedPreferences = context.getSharedPreferences(ON_BOARDING_SHOULD_BE_DISPLAYED, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(ON_BOARDING_SHOULD_BE_DISPLAYED, shouldBeDisplayed).apply()
    }

}
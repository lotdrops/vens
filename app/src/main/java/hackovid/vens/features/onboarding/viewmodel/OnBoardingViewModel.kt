package hackovid.vens.features.onboarding.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.R
import hackovid.vens.common.data.LocalDataSource
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.core.StoresDatabase
import hackovid.vens.common.data.json.RemoteStore
import hackovid.vens.common.data.json.toStore
import hackovid.vens.features.onboarding.OnboardingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnBoardingViewModel(
    private val localStorage: LocalStorage,
    private val database: StoresDatabase,
    private val localDataSource: LocalDataSource<RemoteStore>
) : ViewModel() {

    val screens: MutableLiveData<ArrayList<OnboardingModel>> = fillOnboardScreensInfo()
    var currentPosition = 0
    val isDatabaseLoaded: MutableLiveData<Boolean> = MutableLiveData(true)

    fun hideDashBoard() {
        localStorage.setOnboardScreenVisibility(false)
    }

    fun onBoardScreenShouldBeDisplayed(): Boolean {
        return localStorage.shouldBeDisplayedOnBoardScreen()
    }

    fun loadDatabaseIfIsTheFirstTime() {
        if (localStorage.isDataBaseAlreadyLoaded()) {
            isDatabaseLoaded.value = true
        } else {
            isDatabaseLoaded.value = false
            CoroutineScope(Dispatchers.IO).launch {
                database.storeDao().insertList(localDataSource.readLocalStoreData()?.map { it.toStore() }.apply { Log.d("coords","importListSZ:${this?.size}") } ?: emptyList())
                Log.w("Parse json", "Database processed")
                withContext(Dispatchers.Main) {
                    localStorage.setDataBaseAlreadyLoaded(true)
                    isDatabaseLoaded.value = true
                }
            }
        }
    }

    private fun fillOnboardScreensInfo(): MutableLiveData<ArrayList<OnboardingModel>> {
        val listPagesOnBoard: ArrayList<OnboardingModel> = arrayListOf()
        listPagesOnBoard.add(
            OnboardingModel(
                R.string.on_boarding_screen_title_slide_one,
                R.string.on_boarding_screen_body_slide_one,
                true,
                View.GONE,
                View.VISIBLE
            )
        )
        listPagesOnBoard.add(
            OnboardingModel(
                R.string.on_boarding_screen_title_slide_two,
                R.string.on_boarding_screen_body_slide_two,
                true,
                View.GONE,
                View.VISIBLE
            )
        )
        listPagesOnBoard.add(
            OnboardingModel(
                R.string.on_boarding_screen_title_slide_three,
                R.string.on_boarding_screen_body_slide_three,
                false,
                View.VISIBLE,
                View.INVISIBLE
            )
        )
        return MutableLiveData(listPagesOnBoard)
    }
}

package hackovid.vens.features.onboarding.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.R
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.features.onboarding.OnboardingModel

class OnBoardingViewModel(val localStorage: LocalStorage) : ViewModel() {

    val screens: MutableLiveData<ArrayList<OnboardingModel>> = fillOnboardScreensInfo()
    var currentPosition =  0

    fun hideDashBoard() {
        localStorage.setOnboardScreenVisibility(false)
    }

    fun onBoardScreenShouldBeDisplayed():Boolean {
        return localStorage.shouldBeDisplayedOnBoardScreen()
    }


    private fun fillOnboardScreensInfo(): MutableLiveData<ArrayList<OnboardingModel>> {
        val listPagesOnBoard: ArrayList<OnboardingModel> = arrayListOf()
        listPagesOnBoard.add(
            OnboardingModel(
                R.string.on_boarding_screen_title_slide_one,
                R.string.on_boarding_screen_body_slide_one,
                true,
                false,
                View.GONE,
                View.VISIBLE
            )
        )
        listPagesOnBoard.add(
            OnboardingModel(
                R.string.on_boarding_screen_title_slide_two,
                R.string.on_boarding_screen_body_slide_two,
                true,
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
                true,
                View.VISIBLE,
                View.INVISIBLE
            )
        )
        return MutableLiveData(listPagesOnBoard)
    }

}
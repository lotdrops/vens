package hackovid.vens.features.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.R
import hackovid.vens.common.data.LocalStorage

class OnBoardingViewModel(val localStorage: LocalStorage) : ViewModel() {

    val screens: MutableLiveData<ArrayList<OnboardingModel>> = fillOnboardScreensInfo()

    fun hideDashBoard() {
        localStorage.setOnboardScreenVisibility(false)
    }

    fun onBoardScreenShouldBeDisplayed():Boolean {
        return localStorage.shouldBeDisplayedOnBoardScreen()
    }


    private fun fillOnboardScreensInfo(): MutableLiveData<ArrayList<OnboardingModel>> {
        val listPagesOnBoard: ArrayList<OnboardingModel> = arrayListOf()
        listPagesOnBoard.add(OnboardingModel(R.string.on_boarding_screen_title_slide_one, R.string.on_boarding_screen_body_slide_one,true))
        listPagesOnBoard.add(OnboardingModel(R.string.on_boarding_screen_title_slide_two, R.string.on_boarding_screen_body_slide_two,true))
        listPagesOnBoard.add(OnboardingModel(R.string.on_boarding_screen_title_slide_three, R.string.on_boarding_screen_body_slide_three,false))
        return MutableLiveData(listPagesOnBoard)
    }

}
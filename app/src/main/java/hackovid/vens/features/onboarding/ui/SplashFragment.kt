package hackovid.vens.features.onboarding.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentSplashBinding
import hackovid.vens.features.onboarding.viewmodel.OnBoardingViewModel
import org.koin.android.ext.android.inject

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override val layoutRes = R.layout.fragment_splash
    val viewModel: OnBoardingViewModel by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.onBoardScreenShouldBeDisplayed()) {
            viewModel.loadDatabaseIfIsTheFirstTime()
            observe(viewModel.isDatabaseLoaded) { isDatabaseAlreadyLoaded ->
                if (isDatabaseAlreadyLoaded) {
                    navigateToOnBoardScreen()
                }
            }
        } else {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    private fun navigateToOnBoardScreen() {
            NavHostFragment.findNavController(this)
                .navigate(R.id.nav_to_onboarding_fragment)
    }
}

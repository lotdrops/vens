package hackovid.vens.features.onboarding.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import hackovid.vens.BuildConfig
import hackovid.vens.R
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentSplashBinding
import hackovid.vens.features.login.SelectLoginViewModel
import hackovid.vens.features.onboarding.viewmodel.OnBoardingViewModel
import org.koin.android.ext.android.inject

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override val layoutRes = R.layout.fragment_splash
    val viewModel: OnBoardingViewModel by inject()
    val selectLoginViewModel: SelectLoginViewModel by inject()
    val localStorage: LocalStorage by inject()

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
            navigateToLoginIfUserIsNotLogged()
        }
    }

    override fun setupBinding(binding: FragmentSplashBinding) {
        binding.logo.clipToOutline = true
    }

    private fun navigateToOnBoardScreen() {
        NavHostFragment.findNavController(this).navigate(
            SplashFragmentDirections.navToOnboardingFragment()
        )
    }

    private fun navigateToLoginIfUserIsNotLogged() {
        FirebaseAuth.getInstance().signOut()
        if (selectLoginViewModel.isUserAlreadyLogged()) {
            navigateToMapScreen()
        } else if (!gdprAccepted()) {
            NavHostFragment.findNavController(this).navigate(
                SplashFragmentDirections.navToGdpr()
            )
        } else {
            NavHostFragment.findNavController(this).navigate(
                SplashFragmentDirections.navToLoginFragment()
            )
        }
    }

    private fun gdprAccepted() = localStorage.getTosAcceptedVersion() >= BuildConfig.TOS_VERSION

    private fun navigateToMapScreen() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}

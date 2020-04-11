package hackovid.vens.features.onboarding

import android.content.Intent
import hackovid.vens.MainActivity
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentOnboardingBinding
import org.koin.android.ext.android.inject


class OnBoardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    override val layoutRes = R.layout.fragment_onboarding
    val viewModel: OnBoardingViewModel by inject()
    private val nextButtonClicked = this::onNextButtonClicked

    override fun setupBinding(binding: FragmentOnboardingBinding) {
        setupViewPagerAdapter(binding)
    }

    private fun setupViewPagerAdapter(binding: FragmentOnboardingBinding) {
        observe(viewModel.screens){
            binding.viewpager.adapter = OnboardingAdapter(it, nextButtonClicked)
        }

    }

    private fun onNextButtonClicked(onboardingModel: OnboardingModel) {
        if(!onboardingModel.hasNextScreen) {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        viewModel.hideDashBoard()
        startActivity(Intent(activity, MainActivity::class.java))
    }


}

package hackovid.vens.features.onboarding.ui

import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentOnboardingBinding
import hackovid.vens.features.login.SelectLoginFragmentDirections
import hackovid.vens.features.onboarding.OnboardingModel
import hackovid.vens.features.onboarding.viewmodel.OnBoardingViewModel
import org.koin.android.ext.android.inject

class OnBoardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    override val layoutRes = R.layout.fragment_onboarding
    private lateinit var onBoardBinding: FragmentOnboardingBinding
    val viewModel: OnBoardingViewModel by inject()
    private val nextButtonClicked = this::onNextButtonClicked
    private val discoverButtonClicked = this::onDiscoverButtonClicked
    private val skipButtonClicked = this::onSkipButtonClicked

    override fun setupBinding(binding: FragmentOnboardingBinding) {
        onBoardBinding = binding
        setupViewPagerAdapter(binding)
        onBoardBinding.viewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.currentPosition = position
            }
        })
    }

    private fun setupViewPagerAdapter(binding: FragmentOnboardingBinding) {
        observe(viewModel.screens) {
            binding.viewpager.adapter =
                OnboardingAdapter(
                    it, nextButtonClicked, discoverButtonClicked,
                    skipButtonClicked
                )
        }
    }

    private fun onNextButtonClicked(onboardingModel: OnboardingModel, position: Int) {
        if (!onboardingModel.hasNextScreen) {
            navigateToGdpr()
        } else {
            onBoardBinding.viewpager.currentItem = position
        }
    }

    private fun onDiscoverButtonClicked() {
        navigateToGdpr()
    }

    private fun onSkipButtonClicked() {
        navigateToGdpr()
    }

    private fun navigateToGdpr() {
        viewModel.hideDashBoard()
        NavHostFragment.findNavController(this).navigate(
            OnBoardingFragmentDirections.navToGdpr()
        )
    }
}

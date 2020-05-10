package hackovid.vens.features.login

import androidx.navigation.fragment.NavHostFragment.findNavController
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentGdprBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GdprFragment : BaseFragment<FragmentGdprBinding>() {
    override val layoutRes = R.layout.fragment_gdpr

    private val viewModel: GdprViewModel by viewModel()
    private lateinit var binding: FragmentGdprBinding

    override fun setupBinding(binding: FragmentGdprBinding) {
        this.binding = binding
        binding.viewModel = viewModel
        binding.setupViews()
        subscribeVm()
    }

    private fun FragmentGdprBinding.setupViews() {
        termsOfUse.setOnClickListener {
            findNavController(this@GdprFragment).navigate(GdprFragmentDirections.navToTermsOfUse())
        }
        privacyDetails.setOnClickListener {
            findNavController(this@GdprFragment).navigate(
                GdprFragmentDirections.navToPrivacyPolicy()
            )
        }
    }

    private fun subscribeVm() {
        observe(viewModel.acceptEvent) {
            findNavController(this).navigate(GdprFragmentDirections.navToLoginFragment())
        }
    }
}

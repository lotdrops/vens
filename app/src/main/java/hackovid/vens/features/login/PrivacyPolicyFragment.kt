package hackovid.vens.features.login

import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicyFragment : BaseFragment<FragmentPrivacyPolicyBinding>() {
    override val layoutRes = R.layout.fragment_privacy_policy

    override fun setupBinding(binding: FragmentPrivacyPolicyBinding) {
        binding.close.setOnClickListener { activity?.onBackPressed() }
    }
}

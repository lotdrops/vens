package hackovid.vens.features.login

import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentTermsOfUseBinding

class TermsOfUseFragment : BaseFragment<FragmentTermsOfUseBinding>() {
    override val layoutRes = R.layout.fragment_terms_of_use

    override fun setupBinding(binding: FragmentTermsOfUseBinding) {
        binding.close.setOnClickListener { activity?.onBackPressed() }
    }
}

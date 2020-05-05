package hackovid.vens.features.register

import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentSelectStoreBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectStoreFragment : BaseFragment<FragmentSelectStoreBinding>() {
    override val layoutRes = R.layout.fragment_select_store

    private val viewModel: RegisterViewModel by viewModel()
    private lateinit var binding: FragmentSelectStoreBinding

    override fun setupBinding(binding: FragmentSelectStoreBinding) {
        this.binding = binding
        binding.viewModel = viewModel
    }
}

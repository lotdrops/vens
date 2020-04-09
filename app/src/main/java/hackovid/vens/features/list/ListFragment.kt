package hackovid.vens.features.list

import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : BaseFragment<FragmentListBinding>() {
    override val layoutRes = R.layout.fragment_list

    private val viewModel: ListViewModel by viewModel()

    override fun setupBinding(binding: FragmentListBinding) {
        binding.viewModel = viewModel
    }
}

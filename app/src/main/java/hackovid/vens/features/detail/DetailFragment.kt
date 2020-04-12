package hackovid.vens.features.detail

import androidx.navigation.fragment.navArgs
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.databinding.FragmentDetailBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    override val layoutRes = R.layout.fragment_detail

    private val args: DetailFragmentArgs by navArgs()

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val viewModel: DetailViewModel by viewModel {
        parametersOf(sharedViewModel, args.storeId)
    }

    override fun setupBinding(binding: FragmentDetailBinding) {
        binding.viewModel = viewModel
    }
}

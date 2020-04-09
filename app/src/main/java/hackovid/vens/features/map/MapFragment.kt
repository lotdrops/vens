package hackovid.vens.features.map

import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentMapBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment<FragmentMapBinding>() {
    override val layoutRes = R.layout.fragment_map

    private val viewModel: MapViewModel by viewModel()

    override fun setupBinding(binding: FragmentMapBinding) {
        binding.viewModel = viewModel
    }
}

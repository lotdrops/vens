package hackovid.vens.features.slots

import androidx.appcompat.app.AppCompatActivity
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentSlotsBinding
import hackovid.vens.features.shopprofile.ShopProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SlotsFragment : BaseFragment<FragmentSlotsBinding>() {
    override val layoutRes = R.layout.fragment_slots

    private val viewModel: SlotsViewModel by viewModel()

    override fun setupBinding(binding: FragmentSlotsBinding) {
        binding.viewModel = viewModel
        setupToolbar(binding)
    }

    private fun setupToolbar(binding: FragmentSlotsBinding) {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }
}
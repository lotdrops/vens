package hackovid.vens.features.shopprofile

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentSlotsConfigBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SlotsConfigFragment : BaseFragment<FragmentSlotsConfigBinding>() {
    override val layoutRes = R.layout.fragment_slots_config

    private val viewModel: SlotsConfigViewModel by viewModel()

    override fun setupBinding(binding: FragmentSlotsConfigBinding) {
        binding.viewModel = viewModel
        setupToolbar(binding)
        subscribeToViewModel()
    }

    private fun setupToolbar(binding: FragmentSlotsConfigBinding) {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun subscribeToViewModel() {
        observe(viewModel.scheduleEvent) {
            findNavController().navigate(SlotsConfigFragmentDirections.navToSchedule())
        }
    }
}
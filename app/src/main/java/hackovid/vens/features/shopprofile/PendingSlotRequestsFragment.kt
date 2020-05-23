package hackovid.vens.features.shopprofile

import androidx.appcompat.app.AppCompatActivity
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentPendingSlotRequestsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PendingSlotRequestsFragment : BaseFragment<FragmentPendingSlotRequestsBinding>() {
    override val layoutRes = R.layout.fragment_pending_slot_requests

    private val viewModel: PendingSlotRequestsViewModel by viewModel()

    override fun setupBinding(binding: FragmentPendingSlotRequestsBinding) {
        binding.viewModel = viewModel
        setupToolbar(binding)
    }

    private fun setupToolbar(binding: FragmentPendingSlotRequestsBinding) {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
package hackovid.vens.features.shopprofile

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentShopProfileBinding
import hackovid.vens.features.list.ListFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShopProfileFragment : BaseFragment<FragmentShopProfileBinding>() {
    override val layoutRes = R.layout.fragment_shop_profile

    private val viewModel: ShopProfileViewModel by viewModel()

    override fun setupBinding(binding: FragmentShopProfileBinding) {
        binding.viewModel = viewModel
        setupToolbar(binding)
        subscribeUi()
    }

    private fun setupToolbar(binding: FragmentShopProfileBinding) {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    private fun subscribeUi() {
        observe(viewModel.enableSlotsEvent) {
            findNavController().navigate(ShopProfileFragmentDirections.navToSlotsConfig())
        }
        observe(viewModel.pendingSlotsEvent) {
            findNavController().navigate(ShopProfileFragmentDirections.navToPendingSlotRequests())
        }
        observe(viewModel.scheduleEvent) {
            findNavController().navigate(ShopProfileFragmentDirections.navToSchedule())
        }
    }
}
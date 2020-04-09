package hackovid.vens.features.favourites

import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentFavouritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : BaseFragment<FragmentFavouritesBinding>() {
    override val layoutRes = R.layout.fragment_favourites

    private val viewModel: FavouritesViewModel by viewModel()

    override fun setupBinding(binding: FragmentFavouritesBinding) {
        binding.viewModel = viewModel
    }
}

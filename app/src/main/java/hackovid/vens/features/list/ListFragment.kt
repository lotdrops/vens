package hackovid.vens.features.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : BaseFragment<FragmentListBinding>() {
    override val layoutRes = R.layout.fragment_list

    private val viewModel: ListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(viewModel.selectedStore) {
            NavHostFragment.findNavController(this)
                    .navigate(ListFragmentDirections.navToAdDetail(it))
        }
    }

    override fun setupBinding(binding: FragmentListBinding) {
        binding.viewModel = viewModel
    }
}

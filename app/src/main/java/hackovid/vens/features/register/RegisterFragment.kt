package hackovid.vens.features.register

import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val layoutRes = R.layout.fragment_register

    private val args: RegisterFragmentArgs by navArgs()
    private val viewModel: RegisterViewModel by viewModel {
        parametersOf(args.externalLogin, args.email, args.name, args.lastName)
    }
    private lateinit var binding: FragmentRegisterBinding

    override fun setupBinding(binding: FragmentRegisterBinding) {
        this.binding = binding
        binding.viewModel = viewModel
        subscribeToVm()
        if (viewModel.externalLogin) {
            binding.scrollview.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun subscribeToVm() {
        observe(viewModel.selectStoreEvent) {
            NavHostFragment.findNavController(this).navigate(
                RegisterFragmentDirections.navToSelectStoreFragment()
            )
        }
        observe(viewModel.registerEvent) {
            // TODO create screen and navigate
        }
    }
}

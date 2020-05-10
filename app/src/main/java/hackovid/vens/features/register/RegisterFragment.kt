package hackovid.vens.features.register

import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.UiState
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_login.root_view
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
        observe(viewModel.registerResult) {
            when (it) {
                UiState.Success -> {
                    this.binding.loadingView.visibility = View.GONE
                    // TODO show dialog with about email and go to login
                    // TODO Check register does not send email when coming from googleSignIn
                    /*this.binding.verifyMail.visibility = View.VISIBLE
                    this.binding.verifyMail.text = resources.getString(R.string.register_user_mail_sended)*/
                }
                is UiState.Error -> {
                    this.binding.loadingView.visibility = View.GONE
                    Snackbar.make(root_view, it.errorMessage, Snackbar.LENGTH_SHORT).show();
                }
                UiState.Loading -> {
                    this.binding.loadingView.visibility = View.VISIBLE
                }
            }
        }
        observe(viewModel.registerEvent) {
            // TODO create screen and navigate
        }
    }
}

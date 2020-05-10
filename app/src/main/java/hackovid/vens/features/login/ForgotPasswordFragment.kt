package hackovid.vens.features.login

import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentForgotPasswordBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    override val layoutRes = R.layout.fragment_forgot_password
    private val viewModel: LoginViewModel by inject()
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun setupBinding(binding: FragmentForgotPasswordBinding) {
        this.binding = binding

        observeViewModels()
    }

    private fun observeViewModels() {
        // TODO delete this screen. Do in login screen
        /*observe(viewModel.loginResult) {
            when (it) {
                UIState.Success -> {
                    this.binding.loadingView.visibility = View.GONE
                }
                is UIState.Error -> {
                    this.binding.loadingView.visibility = View.GONE
                    Snackbar.make(root_view, it.errorMessage, Snackbar.LENGTH_LONG).show();
                }
                UIState.Loading -> {
                    this.binding.loadingView.visibility = View.VISIBLE
                }
            }
        }*/
    }
}

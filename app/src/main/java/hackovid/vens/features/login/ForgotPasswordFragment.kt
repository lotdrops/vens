package hackovid.vens.features.login

import android.view.View
import com.google.android.material.snackbar.Snackbar
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.UIState
import hackovid.vens.common.utils.observe
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
        observe(viewModel.loginResult) {
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
        }
    }



}

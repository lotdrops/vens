package hackovid.vens.features.login

import android.content.Intent
import android.view.View
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar
import hackovid.vens.R
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.ui.UiState
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.root_view
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val layoutRes = R.layout.fragment_login
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var binding: FragmentLoginBinding
    private val NO_LOGIN_ERRORS_MESSAGE = 0

    private var initialSoftInputMode: Int? = null

    override fun onStart() {
        super.onStart()
        initialSoftInputMode = activity?.window?.attributes?.softInputMode
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onStop() {
        super.onStop()
        initialSoftInputMode?.let { activity?.window?.setSoftInputMode(it) }
    }

    override fun setupBinding(binding: FragmentLoginBinding) {
        this.binding = binding
        this.binding.viewModel = viewModel
        setupLogin()
        observeViewModels()
    }

    private fun setupLogin() {
        binding.loginButton.setOnClickListener {
            val loginFieldsErrorMessage = validateLoginFields()
            if (loginFieldsErrorMessage == NO_LOGIN_ERRORS_MESSAGE) {
                val user = User(
                    "", "", binding.username.text.toString(), binding.password.text.toString()
                )
                viewModel.login(user)
            } else {
                Snackbar.make(root_view, loginFieldsErrorMessage, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModels() {
        observe(viewModel.loginState) {
            when (it) {
                UiState.Success -> {
                    this.binding.loadingView.visibility = View.GONE
                    navigateToMain()
                }
                is UiState.Error -> {
                    this.binding.loadingView.visibility = View.GONE
                    Snackbar.make(root_view, it.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    this.binding.loadingView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    private fun validateLoginFields() = when {
        binding.username.text.isNullOrEmpty() -> R.string.register_some_empty_field
        binding.password.text.isNullOrEmpty() -> R.string.register_some_empty_field
        binding.password.text?.length ?: 0 < 6 -> R.string.register_password_too_short
        else -> NO_LOGIN_ERRORS_MESSAGE
    }
}

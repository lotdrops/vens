package hackovid.vens.features.register

import android.view.View
import com.google.android.material.snackbar.Snackbar
import hackovid.vens.R
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.UiState
import hackovid.vens.common.ui.UiState.Error
import hackovid.vens.common.utils.isValidEmailField
import hackovid.vens.common.utils.observe
import hackovid.vens.common.utils.passwordHasLessThanSixCharacters
import hackovid.vens.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_select_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val layoutRes = R.layout.fragment_register

    private val registerViewModel: RegisterViewModel by viewModel()
    private lateinit var binding: FragmentRegisterBinding
    private val NO_REGISTER_ERRORS_MESSAGE = 0

    override fun setupBinding(binding: FragmentRegisterBinding) {
        this.binding = binding
        doRegistration()
        observeViewModels()
    }

    private fun doRegistration() {
        binding.registerButton.setOnClickListener {
            val registerFieldsErrorMessage = validateRegisterFields()
            if (registerFieldsErrorMessage == NO_REGISTER_ERRORS_MESSAGE) {
                val user = User(
                    binding.registerFirstName.text.toString(),
                    binding.registerLastName.text.toString(),
                    binding.registerMail.text.toString(),
                    binding.registerPassword.text.toString()
                )
                registerViewModel.registerUser(user)
            } else {
                Snackbar.make(root_view, registerFieldsErrorMessage, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModels() {
        observe(registerViewModel.registerResult) {
            when (it) {
                UiState.Success -> {
                    this.binding.loadingView.visibility = View.GONE
                }
                is Error -> {
                    this.binding.loadingView.visibility = View.GONE
                    Snackbar.make(root_view, it.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
                UiState.Loading -> {
                    this.binding.loadingView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validateRegisterFields() = when {
        binding.registerFirstName.text.isNullOrEmpty() -> R.string.register_some_empty_field
        binding.registerLastName.text.isNullOrEmpty() -> R.string.register_some_empty_field
        binding.registerPassword.text.isNullOrEmpty() -> R.string.register_some_empty_field
        !binding.registerMail.isValidEmailField() -> R.string.register_mail_is_incorrect
        !binding.registerPassword.passwordHasLessThanSixCharacters() ->
            R.string.register_mail_have_less_than_six_characters
        else -> NO_REGISTER_ERRORS_MESSAGE
    }
}

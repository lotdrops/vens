package hackovid.vens.features.register

import android.opengl.Visibility
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import hackovid.vens.R
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.UIState
import hackovid.vens.common.ui.UIState.Error
import hackovid.vens.common.utils.isValidEmailField
import hackovid.vens.common.utils.observe
import hackovid.vens.common.utils.passwordHaveLessThanSixCharacters
import hackovid.vens.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val layoutRes = R.layout.fragment_register

    private val registerViewModel: RegisterViewModel by inject()
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
                Snackbar.make(root_view, registerFieldsErrorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private fun observeViewModels() {
        observe(registerViewModel.registerResult) {
            when (it) {
                UIState.Success -> {
                    this.binding.loadingView.visibility = View.GONE
                    this.binding.verifyMail.visibility = View.VISIBLE
                    this.binding.verifyMail.text = resources.getString(R.string.register_user_mail_sended)
                }
                is Error -> {
                    this.binding.loadingView.visibility = View.GONE
                    Snackbar.make(root_view, it.errorMessage, Snackbar.LENGTH_SHORT).show();
                }
                UIState.Loading -> {
                    this.binding.loadingView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validateRegisterFields(): Int {
        if (binding.registerFirstName.text.isNullOrEmpty()) return R.string.register_some_empty_field
        if (binding.registerLastName.text.isNullOrEmpty()) return R.string.register_some_empty_field
        if (binding.registerPassword.text.isNullOrEmpty()) return R.string.register_some_empty_field
        if (!binding.registerMail.isValidEmailField()) return R.string.register_mail_is_incorrect
        if (!binding.registerPassword.passwordHaveLessThanSixCharacters()) return R.string.register_mail_have_less_than_six_characters
        return NO_REGISTER_ERRORS_MESSAGE
    }


}
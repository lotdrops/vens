package hackovid.vens.features.login

import android.content.Intent
import android.text.SpannableStringBuilder
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.Dialogs
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.utils.hideKeyboard
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val layoutRes = R.layout.fragment_login
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var binding: FragmentLoginBinding

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
        this.binding.fragment = this
        setupViews()
        observeViewModels()
        setForgotPassword()
    }

    private fun setForgotPassword() {
        val validEmail = true
        val context = context
        if (validEmail && context != null) {
            val text = resources.getString(R.string.login_forgot_password)
            val start = text.lastIndexOf(" ") + 1
            val end = text.length
            val spanColor = ContextCompat.getColor(context, R.color.main_purple)
            val spannable = SpannableStringBuilder()
                .append(text.subSequence(0, start))
                .bold { color(spanColor) { append(text.subSequence(start, end)) } }
            binding.forgotPassword.text = spannable
        }
    }

    private fun setupViews() {
        binding.rootView.setOnFocusChangeListener { _, _ -> hideKeyboard() }
        binding.email.setOnFocusChangeListener { _, focused ->
            viewModel.onEmailFocus(focused)
        }
        binding.password.setOnFocusChangeListener { _, focused ->
            viewModel.onPasswordFocus(focused)
        }
    }

    private fun observeViewModels() {
        observe(viewModel.loginOkEvent) {
            hideKeyboard()
            navigateToMain()
        }
        observe(viewModel.recoverOkEvent) {
            context?.let { context ->
                Dialogs.showAlert(
                    context = context,
                    title = R.string.login_recover_password,
                    message = R.string.login_restore_my_password_email
                )
            }
        }
        observe(viewModel.errorEvent) { error ->
            context?.let { context ->
                Dialogs.showAlert(context = context, message = error)
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    fun onForgotPasswordClick() {
        viewModel.recoverPassword()
    }
}

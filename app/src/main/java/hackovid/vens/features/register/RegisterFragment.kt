package hackovid.vens.features.register

import android.content.Intent
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.Dialogs
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.utils.hideKeyboard
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
        setupViews()
        subscribeToVm()
        if (viewModel.externalLogin) {
            binding.scrollview.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun setupViews() {
        binding.rootView.setOnFocusChangeListener { _, _ -> hideKeyboard() }
        binding.name.setOnFocusChangeListener { _, focused ->
            viewModel.onNameFocus(focused)
        }
        binding.lastname.setOnFocusChangeListener { _, focused ->
            viewModel.onLastnameFocus(focused)
        }
        binding.email.setOnFocusChangeListener { _, focused ->
            viewModel.onEmailFocus(focused)
        }
        binding.password.setOnFocusChangeListener { _, focused ->
            viewModel.onPasswordFocus(focused)
        }
        binding.repeatPassword.setOnFocusChangeListener { _, focused ->
            viewModel.onRepeatPasswordFocus(focused)
        }
    }

    private fun subscribeToVm() {
        observe(viewModel.selectStoreEvent) {
            hideKeyboard()
            NavHostFragment.findNavController(this).navigate(
                RegisterFragmentDirections.navToSelectStoreFragment()
            )
        }
        observe(viewModel.registerOkEvent) {
            context?.let { context ->
                Dialogs.showAlert(
                    context = context,
                    title = R.string.register_user_mail_sent_title,
                    message = R.string.register_user_mail_sent,
                    onPositive = { navigateToLogin() }
                )
            }
        }
        observe(viewModel.errorEvent) { error ->
            context?.let { context ->
                Dialogs.showAlert(context = context, message = error)
            }
        }
        observe(viewModel.externalRegisterOkEvent) {
            hideKeyboard()
            navigateToMapScreen()
        }
    }

    private fun navigateToLogin() {
        NavHostFragment.findNavController(this).navigate(
            RegisterFragmentDirections.navToLoginFragment()
        )
    }
    private fun navigateToMapScreen() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}

package hackovid.vens.features.register

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import hackovid.vens.R
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.Dialogs
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_login.*
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
        observe(viewModel.registerOkEvent) {
            context?.let { context ->
                Dialogs.showAlert(
                    context = context,
                    title = null,
                    message = R.string.register_user_mail_sended,
                    onPositive = { navigateToLogin() }
                )
            }
        }
        observe(viewModel.errorEvent) { error ->
            context?.let { context ->
                Dialogs.showAlert(context = context, message = error)
            }
        }
        observe(viewModel.registerEvent) {
            val user = User(viewModel.name.value!!, viewModel.lastName.value!!, viewModel.initialEmail!!)
            if(viewModel.externalLogin) viewModel.registerExternalUser(user)
            else viewModel.registerUser()
        }
        observe(viewModel.registerExternalEvent) {
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
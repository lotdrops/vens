package hackovid.vens.features.login

import android.app.Activity
import android.content.Intent
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.Dialogs
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentSelectLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectLoginFragment : BaseFragment<FragmentSelectLoginBinding>() {

    private var googleLastName: String? = ""
    private var googleName: String? = ""
    private var googleEmail: String? = ""
    private var externalLogin: Boolean = false
    override val layoutRes = R.layout.fragment_select_login
    private val viewModel: SelectLoginViewModel by viewModel()
    private lateinit var binding: FragmentSelectLoginBinding

    val RC_GOOGLE_SIGN_IN_CODE: Int = 1

    override fun setupBinding(binding: FragmentSelectLoginBinding) {
        checkGoogleSignedInAccount()
        this.binding = binding
        binding.viewModel = viewModel

        binding.logo.clipToOutline = true
        binding.loginWithGoogle.setOnClickListener {
            googleSignIn()
        }
        binding.registerView.setOnClickListener {
            findNavController(this).navigate(
                SelectLoginFragmentDirections.navToRegisterFragment()
            )
        }
        binding.registerButton.setOnClickListener {
            findNavController(this).navigate(
                SelectLoginFragmentDirections.navToLoginFragment()
            )
        }
        binding.skipLogin.setOnClickListener {
            navigateToMain()
        }
        subscribeSwitchAccount(binding.switchAccount)
        observeViewModels()
    }

    private fun subscribeSwitchAccount(switchAccount: TextView) {
        observe(viewModel.selectedGoogleAccount) { email ->
            val context = context
            if (email.isNotEmpty() && context != null) {
                val text = resources.getString(R.string.select_login_account_selected, email)
                val start = text.lastIndexOf(" ") + 1
                val end = text.length
                val spanColor = ContextCompat.getColor(context, R.color.main_purple)
                val spannable = SpannableStringBuilder()
                    .append(text.subSequence(0, start))
                    .bold { color(spanColor) { append(text.subSequence(start, end)) } }
                switchAccount.text = spannable
            }
        }
    }

    private fun checkGoogleSignedInAccount() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            viewModel.selectedGoogleAccount.value = account.email
        }
    }

    private fun observeViewModels() {
        observe(viewModel.errorEvent) { error ->
            context?.let { context ->
                Dialogs.showAlert(context = context, message = error)
            }
        }
        observe(viewModel.switchAccountEvent) {
            googleSignOut()
        }
        observe(viewModel.alreadyRegisteredUser) {
            navigateToMain()
        }
        observe(viewModel.navToRegisterFromGoogleEvent) {
            findNavController(this).navigate(
                SelectLoginFragmentDirections.navToRegisterFragment(
                    externalLogin = true,
                    email = googleEmail,
                    name = googleName,
                    lastName = googleLastName
                )
            )
        }
    }

    private fun googleSignIn() {
        activity?.getGoogleSignInClient()?.signInIntent?.let { intent ->
            startActivityForResult(intent, RC_GOOGLE_SIGN_IN_CODE)
        }
    }

    private fun googleSignOut() {
        activity?.getGoogleSignInClient()?.revokeAccess()?.addOnSuccessListener {
            viewModel.selectedGoogleAccount.value = ""
            googleSignIn()
        }
    }

    private fun Activity.getGoogleSignInClient() = GoogleSignIn.getClient(
        this,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN_CODE && resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(singIn: Task<GoogleSignInAccount>) {

        try {
            val account = singIn.getResult(ApiException::class.java)
            if (account != null) {
                externalLogin = true
                googleEmail = account.email
                googleName = account.givenName
                googleLastName = account.familyName
                firebaseGoogleAuthentication(account)
            } else {
                context?.let { context -> Dialogs.showAlert(context) }
            }
        } catch (exception: ApiException) {
            context?.let { context -> Dialogs.showAlert(context) }
        }
    }

    private fun firebaseGoogleAuthentication(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        viewModel.loginWithGoogle(credential)
    }

    private fun navigateToMain() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}

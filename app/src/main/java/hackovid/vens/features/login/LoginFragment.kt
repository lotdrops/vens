package hackovid.vens.features.login

import android.content.Intent
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import hackovid.vens.R
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.ui.UIState
import hackovid.vens.common.utils.observe
import hackovid.vens.common.utils.passwordHaveLessThanSixCharacters
import hackovid.vens.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val layoutRes = R.layout.fragment_login
    private val loginViewModel: LoginViewModel by inject()
    private lateinit var binding: FragmentLoginBinding
    private val NO_LOGIN_ERRORS_MESSAGE = 0

    val RC_GOOGLE_SIGN_IN_CODE: Int = 1


    override fun setupBinding(binding: FragmentLoginBinding) {
        this.binding = binding
        setupLogin()
        setupGoogleLogin()

        binding.registerView.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.nav_to_register_fragment)
        }
        observeViewModels()
    }

    private fun setupLogin() {
        binding.loginButton.setOnClickListener {
            val loginFieldsErrorMessage = validateLoginFields()
            if (loginFieldsErrorMessage == NO_LOGIN_ERRORS_MESSAGE) {
                val user = User(
                    "", "", binding.username.text.toString(), binding.password.text.toString()
                )
                loginViewModel.login(user)
            } else {
                Snackbar.make(root_view, loginFieldsErrorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private fun setupGoogleLogin() {
        binding.loginWithGoogle.setOnClickListener {
            val mGoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val mGoogleSignInClient =
                GoogleSignIn.getClient(requireActivity(), mGoogleSignInOptions)
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN_CODE)
        }
    }

    private fun observeViewModels() {
        observe(loginViewModel.loginResult) {
            when (it) {
                UIState.Success -> {
                    this.binding.loadingView.visibility = View.GONE
                    navigateToMain()
                }
                is UIState.Error -> {
                    this.binding.loadingView.visibility = View.GONE
                    Snackbar.make(root_view, it.errorMessage, Snackbar.LENGTH_SHORT).show();
                }
                UIState.Loading -> {
                    this.binding.loadingView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(singIn: Task<GoogleSignInAccount>) {
        try {
            val account = singIn.getResult(ApiException::class.java)
            firebaseGoogleAuthentication(account!!)
        } catch (exception: ApiException) {
            Snackbar.make(root_view, R.string.login_generic_error, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun firebaseGoogleAuthentication(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        loginViewModel.loginWithGoogle(credential)
    }

    private fun navigateToMain() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    private fun validateLoginFields() = when {
        binding.username.text.isNullOrEmpty() -> R.string.register_some_empty_field
        binding.password.text.isNullOrEmpty() -> R.string.register_some_empty_field
        !binding.password.passwordHaveLessThanSixCharacters() ->
            R.string.register_mail_have_less_than_six_characters
        else -> NO_LOGIN_ERRORS_MESSAGE
    }


}
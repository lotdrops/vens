package hackovid.vens.features.login

import android.content.Intent
import android.view.View
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.MainActivity
import hackovid.vens.common.ui.UiState
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentSelectLoginBinding
import kotlinx.android.synthetic.main.fragment_select_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectLoginFragment : BaseFragment<FragmentSelectLoginBinding>() {

    override val layoutRes = R.layout.fragment_select_login
    private val viewModel: SelectLoginViewModel by viewModel()
    private lateinit var binding: FragmentSelectLoginBinding

    val RC_GOOGLE_SIGN_IN_CODE: Int = 1

    override fun setupBinding(binding: FragmentSelectLoginBinding) {
        this.binding = binding
        binding.viewModel = viewModel
        setupGoogleLogin()

        binding.logo.clipToOutline = true
        binding.registerView.setOnClickListener {
            findNavController(this).navigate(SelectLoginFragmentDirections.navToRegisterFragment())
        }
        binding.loginButton.setOnClickListener {
            findNavController(this).navigate(SelectLoginFragmentDirections.navToLoginFragment())
        }
        binding.skipLogin.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
        observeViewModels()
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
        viewModel.loginWithGoogle(credential)
    }

    private fun navigateToMain() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }
}

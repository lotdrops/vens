package hackovid.vens.features.login

import android.app.Activity
import android.content.Intent
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
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
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
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
        observe(viewModel.switchAccountEvent) {
            googleSignOut()
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
        if (requestCode == RC_GOOGLE_SIGN_IN_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(singIn: Task<GoogleSignInAccount>) {
        try {
            val account = singIn.getResult(ApiException::class.java)
            Log.d("asddd", "Google Login, email:${account?.email}, dispName:${account?.displayName}, famName:${account?.familyName}, givenName:${account?.givenName}, account:$account")
            if (account != null) {
                findNavController(this).navigate(
                    SelectLoginFragmentDirections.navToRegisterFragment(
                        externalLogin = true,
                        email = account.email,
                        name = account.givenName,
                        lastName = account.familyName
                    )
                )
                // TODO move to screen after GDPR?
                // firebaseGoogleAuthentication(account)
            } else {
                Snackbar.make(root_view, R.string.login_generic_error, Snackbar.LENGTH_SHORT).show()
            }
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

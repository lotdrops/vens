package hackovid.vens.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import hackovid.vens.common.data.login.FirebaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.UiState
import hackovid.vens.features.register.RegisterUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val dataSource: RemoteDataSource<FirebaseResponse>,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val loginState = MutableLiveData<UiState>()
    val recoverState = MutableLiveData<UiState>()

    val showProgress = loginState.map { it == UiState.Loading }
    val enableButtons = true//loginState.map { it != UiState.Loading }

    fun isUserAlreadyLogged(): Boolean {
        return dataSource.isUserAlreadyLoged().success
    }

    fun login(user: User) = viewModelScope.launch {
        val result = registerUseCase.login(user)
        loginState.value = UiState.Loading
       // val result = dataSource.login(user)
        if (result.success) {
            loginState.value = UiState.Success
        } else {
            loginState.value = result.error?.errorMessage?.let {
                UiState.Error(it)
            }
        }
    }

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            recoverState.value = UiState.Loading
            val result = dataSource.forgotPassword(email)
            recoverState.value = UiState.Success

            /*if (result.success) {
                recoverState.value = UiState.Success
            } else {
                recoverState.value = result.error?.errorMessage?.let {
                    UiState.Error(it)

                }
            }*/
        }
    }

    fun loginWithGoogle(credentials: AuthCredential) = viewModelScope.launch {
        loginState.value = UiState.Loading
        val result = dataSource.loginWithGoogle(credentials)
        if (result.success) {
            loginState.value = UiState.Success
        } else {
            loginState.value = result.error?.errorMessage?.let {
                UiState.Error(it)
            }
        }
    }
}

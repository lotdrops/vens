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
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.features.register.RegisterUseCase
import kotlinx.coroutines.launch

class SelectLoginViewModel(
    private val dataSource: RemoteDataSource<FirebaseResponse>,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val switchAccountEvent = SingleLiveEvent<Unit>()

    val loginState = MutableLiveData<UiState>(UiState.Idle)
    val showProgress = loginState.map { it == UiState.Loading }
    val enableButtons = loginState.map { it != UiState.Loading }

    val selectedGoogleAccount = MutableLiveData("")

    fun isUserAlreadyLogged(): Boolean {
        return registerUseCase.isUserAlreadyLoged()
    }

    fun login(user: User) = viewModelScope.launch {
        loginState.value = UiState.Loading
        val result = dataSource.login(user)
        if (result.success) {
            loginState.value = UiState.Success
        } else {
            loginState.value = result.error?.errorMessage?.let {
                UiState.Error(it)
            }
        }
    }

    fun loginWithGoogle(credentials: AuthCredential) = viewModelScope.launch {
        loginState.value = UiState.Loading
        val result = registerUseCase.loginWithGoogle(credentials)
        if (result.success) {
            loginState.value = UiState.Success
        } else {
            loginState.value = result.error?.errorMessage?.let {
                UiState.Error(it)
            }
        }
    }

    fun onSwitchAccountClicked() {
        switchAccountEvent.call()
    }
}

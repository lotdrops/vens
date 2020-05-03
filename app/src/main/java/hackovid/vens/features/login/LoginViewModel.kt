package hackovid.vens.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import hackovid.vens.common.data.login.FireBaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.UIState
import kotlinx.coroutines.launch

class LoginViewModel(private val dataSource: RemoteDataSource<FireBaseResponse>) : ViewModel() {

    var loginResult = MutableLiveData<UIState>()
    fun isUserAlreadyLogged(): Boolean {
        return dataSource.isUserAlreadyLoged().success
    }

    fun login(user: User) = viewModelScope.launch {
        loginResult.value = UIState.Loading
        val result = dataSource.login(user)
        if (result.success) {
            loginResult.value = UIState.Success
        } else {
            loginResult.value = result.error?.errorMessage?.let {
                UIState.Error(it)
            }
        }
    }

    fun loginWithGoogle(credentials: AuthCredential) = viewModelScope.launch {
        loginResult.value = UIState.Loading
        val result = dataSource.loginWithGoogle(credentials)
        if (result.success) {
            loginResult.value = UIState.Success
        } else {
            loginResult.value = result.error?.errorMessage?.let {
                UIState.Error(it)
            }
        }
    }
}
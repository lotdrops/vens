package hackovid.vens.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import hackovid.vens.common.data.login.FireBaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel(private val datasource: RemoteDataSource<FireBaseResponse>) : ViewModel() {

    var loginResult =  MutableLiveData<UIState>()
    fun isUserAlreadyLoged(): Boolean {
        return datasource.isUserAlreadyLoged().success
    }

    fun login(user: User) {
        loginResult.value = UIState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val result = datasource.login(user)
            withContext(Dispatchers.Main) {
                if(result.success) {
                    loginResult.value = UIState.Success
                } else {
                    loginResult.value = result.error?.errorMessage?.let {
                        UIState.Error(it)
                    }
                }
            }
        }
    }

    fun loginWithGoogle(credentials: AuthCredential) {
        loginResult.value = UIState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val result = datasource.loginWithGoogle(credentials)
            withContext(Dispatchers.Main) {
                if(result.success) {
                    loginResult.value = UIState.Success
                } else {
                    loginResult.value = result.error?.errorMessage?.let {
                        UIState.Error(it)
                    }
                }
            }
        }
    }






}
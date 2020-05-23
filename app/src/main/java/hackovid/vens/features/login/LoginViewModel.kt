package hackovid.vens.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import hackovid.vens.R
import hackovid.vens.common.data.login.FirebaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.features.register.RegisterUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val dataSource: RemoteDataSource<FirebaseResponse>,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val loading = MutableLiveData(false)
    val enableButtons = loading.map { !it }

    val errorEvent = SingleLiveEvent<Int>()
    val recoverOkEvent = SingleLiveEvent<Unit>()
    val loginOkEvent = SingleLiveEvent<Unit>()

    fun login(user: User) = viewModelScope.launch {
        loading.value = true
        val result = registerUseCase.login(user)
        loading.value = false
        if (result.success) {
            loginOkEvent.call()
        } else {
            errorEvent.value = result.error?.errorMessage ?: R.string.generic_error_message
        }
    }

    fun recoverPassword() {
        if (loading.value == true) return

        viewModelScope.launch {
            val email = email.value
            if (email.isNullOrEmpty()) {
                errorEvent.value = R.string.register_mail_is_incorrect
            } else {
                loading.value = true
                val result = dataSource.forgotPassword(email)
                loading.value = false
                if (result.success) {
                    recoverOkEvent.call()
                } else {
                    errorEvent.value = result.error?.errorMessage ?: R.string.generic_error_message
                }
            }
        }
    }
}

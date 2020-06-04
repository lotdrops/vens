package hackovid.vens.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import hackovid.vens.R
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.features.register.RegisterFieldsValidator
import hackovid.vens.features.register.RegisterUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val dataSource: RemoteDataSource,
    private val registerUseCase: RegisterUseCase,
    private val validator: RegisterFieldsValidator
) : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val loading = MutableLiveData(false)
    val enableButtons = loading.map { !it }
    val emailError = MutableLiveData<Int?>(null)
    val passwordError = MutableLiveData<Int?>(null)

    val errorEvent = SingleLiveEvent<Int>()
    val recoverOkEvent = SingleLiveEvent<Unit>()
    val loginOkEvent = SingleLiveEvent<Unit>()
    val navBackEvent = SingleLiveEvent<Unit>()

    fun onBackClicked() {
        navBackEvent.call()
    }

    fun onLoginClicked() = viewModelScope.launch {
        val email = email.value
        val password = password.value
        validateFields()
        if (email != null && password != null && emailError.value == null &&
            passwordError.value == null
        ) {
            val user = User("", "", email, password)
            loading.value = true
            val result = registerUseCase.login(user)
            loading.value = false
            if (result is Ok) loginOkEvent.call()
            else errorEvent.value = (result as Err).error
        }
    }

    private fun validateFields() {
        emailError.value =
            if (!validator.isValidEmail(email.value)) R.string.register_mail_is_incorrect else null
        passwordError.value =
            if (!validator.isValidPassword(password.value)) R.string.register_password_too_short
            else null
    }

    fun onEmailFocus(focused: Boolean) {
        if (focused) emailError.value = null
    }

    fun onPasswordFocus(focused: Boolean) {
        if (focused) passwordError.value = null
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
                if (result is Ok) recoverOkEvent.call()
                else errorEvent.value = (result as Err).error
            }
        }
    }
}

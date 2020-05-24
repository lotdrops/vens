package hackovid.vens.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.google.firebase.auth.AuthCredential
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.features.register.RegisterUseCase
import kotlinx.coroutines.launch

class SelectLoginViewModel(
    private val dataSource: RemoteDataSource,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val navToMapEvent = SingleLiveEvent<Unit>()
    val navToRegisterFromGoogleEvent = SingleLiveEvent<Unit>()
    val switchAccountEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<Int>()

    val showProgress = MutableLiveData(false)
    val enableButtons = showProgress.map { !it }

    val selectedGoogleAccount = MutableLiveData("")

    fun isUserAlreadyLogged(): Boolean {
        return registerUseCase.isUserAlreadyLoged()
    }

    fun loginWithGoogle(credentials: AuthCredential) = viewModelScope.launch {
        showProgress.value = true
        registerUseCase.loginWithGoogle(credentials)
            .andThen { registerUseCase.isUserRegistered() }
            .onSuccess { isRegistered ->
                if (isRegistered) navToMapEvent.call()
                else navToRegisterFromGoogleEvent.call()
            }.onFailure { errorEvent.value = it }
        showProgress.value = false
    }

    fun onSwitchAccountClicked() {
        switchAccountEvent.call()
    }
}

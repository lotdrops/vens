package hackovid.vens.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.google.firebase.auth.AuthCredential
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.features.register.RegisterUseCase
import kotlinx.coroutines.launch

class SelectLoginViewModel(
    private val dataSource: RemoteDataSource,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val switchAccountEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<Int>()
    val loginWithGoogleOkEvent = SingleLiveEvent<Unit>()

    val showProgress = MutableLiveData(false)
    val enableButtons = showProgress.map { !it }

    val selectedGoogleAccount = MutableLiveData("")

    fun isUserAlreadyLogged(): Boolean {
        return registerUseCase.isUserAlreadyLoged()
    }

    fun loginWithGoogle(credentials: AuthCredential) = viewModelScope.launch {
        showProgress.value = true
        val result = registerUseCase.loginWithGoogle(credentials)
        showProgress.value = false
        if (result is Ok) loginWithGoogleOkEvent.call()
        else errorEvent.value = (result as Err).error
    }

    fun onSwitchAccountClicked() {
        switchAccountEvent.call()
    }
}

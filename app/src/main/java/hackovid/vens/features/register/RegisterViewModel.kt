package hackovid.vens.features.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.login.FirebaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.UiState
import kotlinx.coroutines.launch

class RegisterViewModel(private val dataSource: RemoteDataSource<FirebaseResponse>) : ViewModel() {

    var registerResult = MutableLiveData<UiState>()

    fun registerUser(user: User) = viewModelScope.launch {
        registerResult.value = UiState.Loading
        val result = dataSource.registerUser(user)
        if (result.success) {
            registerResult.value = UiState.Success
        } else {
            registerResult.value = result.error?.errorMessage?.let {
                UiState.Error(it)
            }
        }
    }
}

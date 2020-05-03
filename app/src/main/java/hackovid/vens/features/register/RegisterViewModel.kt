package hackovid.vens.features.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hackovid.vens.common.data.login.FireBaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val dataSource: RemoteDataSource<FireBaseResponse>) : ViewModel() {

    var registerResult = MutableLiveData<UIState>()

    fun registerUser(user: User) = viewModelScope.launch {
        registerResult.value = UIState.Loading
        val result = dataSource.registerUser(user)
        if (result.success) {
            registerResult.value = UIState.Success
        } else {
            registerResult.value = result.error?.errorMessage?.let {
                UIState.Error(it)
            }
        }
    }

}
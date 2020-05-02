package hackovid.vens.features.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackovid.vens.R
import hackovid.vens.common.data.login.FireBaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val datasource: RemoteDataSource<FireBaseResponse>) : ViewModel() {

    var registerResult =  MutableLiveData<UIState>()

    fun registerUser(user: User) {
        registerResult.value = UIState.Loading
        CoroutineScope(Dispatchers.IO).launch {
             val result = datasource.registerUser(user)
            withContext(Dispatchers.Main) {
                if(result.success) {
                    registerResult.value = UIState.Success
                } else {
                    registerResult.value = result.error?.errorMessage?.let {
                        UIState.Error(it)
                    }
                }
            }
        }
    }

}
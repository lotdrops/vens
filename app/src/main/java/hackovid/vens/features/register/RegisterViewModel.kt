package hackovid.vens.features.register

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
import kotlinx.coroutines.launch

class RegisterViewModel(
    val externalLogin: Boolean,
    val initialEmail: String?,
    private val initialName: String?,
    private val initialLastname: String?,
    private val dataSource: RemoteDataSource,
    private val validator: RegisterFieldsValidator,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val selectStoreEvent = SingleLiveEvent<Unit>()
    val registerEvent = SingleLiveEvent<Unit>()

    val name = MutableLiveData("")
    val lastName = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val repeatPassword = MutableLiveData("")
    val nameError = MutableLiveData<Int?>(0)
    val lastNameError = MutableLiveData<Int?>(0)
    val emailError = MutableLiveData<Int?>(0)
    val passwordError = MutableLiveData<Int?>(0)
    val repeatPasswordError = MutableLiveData<Int?>(0)

    val isShopOwner = MutableLiveData(false)
    val buttonText = isShopOwner.map { isShopOwner ->
        if (isShopOwner) R.string.register_select_store_button
        else R.string.register_title
    }
    val shopOwnerIcon = isShopOwner.map { isShopOwner ->
        if (isShopOwner) R.drawable.ic_store
        else R.drawable.ic_person
    }

    val loading = MutableLiveData(false)
    val enableButtons = loading.map { !it }
    val errorEvent = SingleLiveEvent<Int>()
    val registerOkEvent = SingleLiveEvent<Unit>()
    val registerExternalEvent = SingleLiveEvent<Unit>()

    init {
        name.value = if(initialName == ("null")) "" else initialName
        lastName.value = if(initialLastname == ("null")) "" else initialLastname
    }

    fun onBuyerClicked() {
        isShopOwner.value = false
    }

    fun onOwnerClicked() {
        isShopOwner.value = true
    }

    fun onButtonClick() {
        validateFields()
        if (!anyErrorRemaining()) {
            if (isShopOwner.value == true) selectStoreEvent.call()
            else registerEvent.call()
        }
    }

    private fun validateFields() {
        nameError.value =
            if (!validator.isValidName(name.value)) R.string.register_empty_field_error else null
        lastNameError.value =
            if (!validator.isValidName(lastName.value)) R.string.register_empty_field_error else null
        if(!externalLogin) {
            emailError.value =
            if (!validator.isValidEmail(email.value)) R.string.register_mail_is_incorrect else null
            passwordError.value =
                if (!validator.isValidPassword(password.value)) R.string.register_password_too_short
                else null
            repeatPasswordError.value =
                if (password.value != repeatPassword.value) R.string.register_passwords_do_not_match
                else null
        } else {
            emailError.value = null
            passwordError.value = null
            repeatPasswordError.value = null
        }
    }

    private fun anyErrorRemaining() = nameError.value != null || lastNameError.value != null ||
            emailError.value != null || passwordError.value != null ||
            repeatPasswordError.value != null

    fun registerUser() = viewModelScope.launch {
        loading.value = true
        val name = name.value
        val lastName = lastName.value
        val email = email.value
        val password = password.value
        if (name != null && lastName != null && email != null && password != null) {
            val user = User(name, lastName, email, password)
            val result = registerUseCase.register(user)
            loading.value = false
            if (result is Ok) registerOkEvent.call()
            else errorEvent.value = (result as Err).error
        }
    }

    fun registerExternalUser(user: User) {
        viewModelScope.launch {
            registerUseCase.storeUserOnFirestoreIfNotExists(user)
            registerEvent.call()
        }
    }
}

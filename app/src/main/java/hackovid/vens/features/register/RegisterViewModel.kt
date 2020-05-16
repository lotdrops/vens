package hackovid.vens.features.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import hackovid.vens.R
import hackovid.vens.common.data.login.FirebaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.common.ui.UiState
import hackovid.vens.common.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class RegisterViewModel(
    val externalLogin: Boolean,
    val initialEmail: String?,
    private val initialName: String?,
    private val initialLastname: String?,
    private val dataSource: RemoteDataSource<FirebaseResponse>,
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

    var registerResult = MutableLiveData<UiState>()

    init {
        if (externalLogin) {
            name.value = initialName ?: ""
            lastName.value = initialLastname ?: ""
        }
    }

    fun onBuyerClicked() {
        isShopOwner.value = false
    }

    fun onOwnerClicked() {
        isShopOwner.value = true
    }

    fun onButtonClick() {
        selectStoreEvent.call()
        validateFields()
        //if (!anyErrorRemaining()) {
            if (isShopOwner.value == true) {
                selectStoreEvent.call()
            } else {
                registerEvent.call()
            }
       // }
    }

    private fun validateFields() {
        nameError.value =
            if (validator.isValidName(name.value)) R.string.register_empty_field_error else null
        lastNameError.value =
            if (validator.isValidName(lastName.value)) R.string.register_empty_field_error else null
        emailError.value =
            if (!validator.isValidEmail(email.value)) R.string.register_mail_is_incorrect else null
        passwordError.value =
            if (!validator.isValidPassword(password.value)) R.string.register_password_too_short
            else null
        repeatPasswordError.value =
            if (password.value != repeatPassword.value) R.string.register_passwords_do_not_match
            else null
    }

    private fun anyErrorRemaining() = nameError.value != null || lastNameError.value != null ||
            emailError.value != null || passwordError.value != null ||
            repeatPasswordError.value != null

    fun registerUser() = viewModelScope.launch {
        registerResult.value = UiState.Loading
        val user = User(name.value!!, lastName.value!!, email.value!!, password.value!! )
        val result = registerUseCase.register(user)
        if (result.success) {
            registerResult.value = UiState.Success
        } else {
            registerResult.value = result.error?.errorMessage?.let {
                UiState.Error(it)
            }
        }
    }

    fun registerExternalUser(user: User) {
        registerUseCase.storeUserOnFirestoreIfNotExists(user)
    }
}

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

class RegisterViewModel(private val dataSource: RemoteDataSource<FirebaseResponse>) : ViewModel() {

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

    fun onBuyerClicked() {
        isShopOwner.value = false
    }

    fun onOwnerClicked() {
        isShopOwner.value = true
    }

    fun onButtonClick() {
        validateFields()
        if (!anyErrorRemaining()) {
            if (isShopOwner.value == true) {
                selectStoreEvent.call()
            } else {
                registerEvent.call()
            }
        }
    }

    private fun validateFields() {
        nameError.value =
            if (name.value.isNullOrBlank()) R.string.register_empty_field_error else null
        lastNameError.value =
            if (lastName.value.isNullOrBlank()) R.string.register_empty_field_error else null
        emailError.value =
            if (!email.value.isValidEmail()) R.string.register_mail_is_incorrect else null
        passwordError.value =
            if (!password.value.isValidPassword()) R.string.register_password_too_short else null
        repeatPasswordError.value =
            if (password.value != repeatPassword.value) R.string.register_passwords_do_not_match
            else null
    }

    private fun anyErrorRemaining() = nameError.value != null || lastNameError.value != null ||
            emailError.value != null || passwordError.value != null ||
            repeatPasswordError.value != null

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

    private fun String?.isValidEmail() =
        !this.isNullOrBlank() && MAIL_REGEX.toRegex().matches(this)

    private fun String?.isValidPassword() = this != null && this.length >= MIN_PWD_LENGTH
}
private const val MAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
private const val MIN_PWD_LENGTH = 6

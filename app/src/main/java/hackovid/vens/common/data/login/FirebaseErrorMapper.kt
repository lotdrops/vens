package hackovid.vens.common.data.login

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import hackovid.vens.R
import hackovid.vens.common.ui.UiState

const val errorDefaultMessage = "Default message error firebase"
class FirebaseErrorMapper {

    fun mapToUiError(firebaseException: FirebaseException): UiState.Error {
        when (firebaseException) {
            is FirebaseAuthInvalidCredentialsException -> {
                return UiState.Error(R.string.login_incorrect_user_or_password)
            }
            is FirebaseAuthEmailException -> {
                return UiState.Error(R.string.login_incorrect_mail)
            }
            is FirebaseAuthActionCodeException -> {
                return UiState.Error(R.string.login_incorrect_user_or_password)
            }
            is FirebaseAuthInvalidUserException -> {
                return UiState.Error(R.string.login_user_not_valid)
            }
            is FirebaseTooManyRequestsException -> {
                return UiState.Error(R.string.login_too_many_times)
            }
            is FirebaseAuthUserCollisionException -> {
                return UiState.Error(R.string.register_user_already_exist)
            }
            is UserNotVerifiedFirebaseException -> {
                return UiState.Error(R.string.register_user_mail_not_verified)
            }
            else -> {
                return UiState.Error(R.string.login_generic_error)
            }
        }
    }
}
class UserNotVerifiedFirebaseException : FirebaseException(errorDefaultMessage)

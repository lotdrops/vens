package hackovid.vens.common.data.login

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import hackovid.vens.R
import hackovid.vens.common.ui.UIState

class FirebaseErrorMapper {

    fun mapToUiError(firebaseException: FirebaseException): UIState.Error {
        when (firebaseException) {
            is FirebaseAuthInvalidCredentialsException -> {
                return UIState.Error(R.string.login_incorrect_user_or_password)
            }
            is FirebaseAuthEmailException -> {
                return UIState.Error(R.string.login_incorrect_mail)
            }
            is FirebaseAuthActionCodeException -> {
                return UIState.Error(R.string.login_incorrect_user_or_password)
            }
            is FirebaseAuthInvalidUserException -> {
                return UIState.Error(R.string.login_user_not_valid)
            }
            is FirebaseTooManyRequestsException -> {
                return UIState.Error(R.string.login_too_many_times)
            }
            is FirebaseAuthUserCollisionException -> {
                return UIState.Error(R.string.register_user_already_exist)
            }
            else -> {
                return UIState.Error(R.string.login_generic_error)
            }
        }
    }
}
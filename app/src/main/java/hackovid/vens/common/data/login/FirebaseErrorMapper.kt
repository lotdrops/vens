package hackovid.vens.common.data.login

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import hackovid.vens.R

const val errorDefaultMessage = "Default message error firebase"
class FirebaseErrorMapper {
    fun mapToError(firebaseException: Throwable): Int = when (firebaseException) {
        is FirebaseAuthInvalidCredentialsException -> R.string.login_incorrect_user_or_password
        is FirebaseAuthEmailException -> R.string.login_incorrect_mail
        is FirebaseAuthActionCodeException -> R.string.login_incorrect_user_or_password
        is FirebaseAuthInvalidUserException -> R.string.login_user_not_valid
        is FirebaseTooManyRequestsException -> R.string.login_too_many_times
        is FirebaseAuthUserCollisionException -> R.string.register_user_already_exist
        is UserNotVerifiedFirebaseException -> R.string.register_user_mail_not_verified
        else -> R.string.generic_error_message
    }
}
class UserNotVerifiedFirebaseException : FirebaseException(errorDefaultMessage)

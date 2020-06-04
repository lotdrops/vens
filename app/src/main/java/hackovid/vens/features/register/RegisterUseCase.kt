package hackovid.vens.features.register

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onSuccess
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hackovid.vens.R
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterUseCase(
    private val auth: FirebaseAuth,
    private val remoteDataSource: RemoteDataSource,
    private val localStore: LocalStorage,
    private val db: FirebaseFirestore

) {

    suspend fun register(registerUser: User) = remoteDataSource.registerUser(registerUser)
        .onSuccess {
            localStore.setAlreadyLoggedIn(false)
            localStore.setUserDataOnRegister(registerUser.copy(password = ""))
        }

    suspend fun login(user: User) = remoteDataSource.login(user).andThen { storeUserIfFirstTime() }

    private suspend fun storeUserIfFirstTime(): Result<Unit, Int> {
        val userStored = localStore.getUserDataOnRegister()
        if (userStored != null && !localStore.isAlreadyLoggedIn()) {
            val isRegisteredResult = isRegisteredAndStoreIfNot(userStored)
            return if (isRegisteredResult is Ok) {
                localStore.setAlreadyLoggedIn(true)
                localStore.setUserDataOnRegister(null)
                Ok(Unit)
            } else {
                isRegisteredResult.map { Unit }
            }
        }
        return Ok(Unit)
    }

    suspend fun loginWithGoogle(credentials: AuthCredential) =
        remoteDataSource.loginWithGoogle(credentials)

    suspend fun isUserRegistered(): Result<Boolean, Int> = suspendCoroutine { continuation ->
        auth.uid?.let { uid ->
            val userDataRef = db.collection(COLLECTION_USERS).document(uid)
            userDataRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Ok(task.result?.exists() == true))
                } else {
                    continuation.resume(Err(R.string.generic_error_message))
                }
            }
        } ?: continuation.resume(Err(R.string.generic_error_message))
    }

    suspend fun isRegisteredAndStoreIfNot(userStored: User) =
        isUserRegistered().andThen { isRegistered ->
            localStore.setAlreadyLoggedIn(true)
            if (!isRegistered) {
                auth.uid?.let { uid ->
                    val userDataRef = db.collection(COLLECTION_USERS).document(uid)
                    userDataRef.set(userStored)
                    Ok(isRegistered)
                } ?: Err(R.string.generic_error_message)
            } else {
                Ok(isRegistered)
            }
        }

    fun isUserAlreadyLoged() = localStore.isAlreadyLoggedIn()

    companion object {
        private const val COLLECTION_USERS = "Users"
    }
}

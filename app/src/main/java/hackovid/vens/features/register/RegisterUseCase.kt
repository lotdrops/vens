package hackovid.vens.features.register

import android.util.Log
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User

class RegisterUseCase(
    private val auth: FirebaseAuth,
    private val remoteDataSource: RemoteDataSource,
    private val localStore: LocalStorage,
    private val db: FirebaseFirestore

) {

    suspend fun register(registerUser: User) = remoteDataSource.registerUser(registerUser)
        .onSuccess {
            localStore.setFirstLogin(true)
            localStore.setUserDataOnRegister(registerUser.copy(password = ""))
        }

    suspend fun login(user: User) =
        remoteDataSource.login(user).onSuccess { storeUserIfFirstTime() }

    private fun storeUserIfFirstTime() {
        val userStored = localStore.getUserDataOnRegister()
        if (userStored != null && localStore.isFirstLogin()) {
            storeUserOnFirestoreIfNotExists(userStored)
            localStore.setFirstLogin(false)
            localStore.setUserDataOnRegister(null)
        }
    }

    suspend fun loginWithGoogle(credentials: AuthCredential) =
        remoteDataSource.loginWithGoogle(credentials)

    fun storeUserOnFirestoreIfNotExists(userStored: User) {
        auth.uid?.let { uid ->
            val userDataRef = db.collection(COLLECTION_USERS).document(uid)
            userDataRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result?.exists() == false) userDataRef.set(userStored)
                } else {
                    Log.e("firestore", task.exception?.localizedMessage)
                }
            }
        }
    }

    fun isUserAlreadyLoged() = !localStore.isFirstLogin()

    companion object {
        private const val COLLECTION_USERS = "Users"
    }
}

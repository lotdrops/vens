package hackovid.vens.features.register

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.login.FirebaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User

class RegisterUseCase(
    private val auth: FirebaseAuth,
    private val remoteDataSource: RemoteDataSource<FirebaseResponse>,
    private val localStore: LocalStorage,
    private val db: FirebaseFirestore

) {

    suspend fun register(registerUser: User): FirebaseResponse {
        val result = remoteDataSource.registerUser(registerUser)
        if (result.success) {
            localStore.setFirstLogin(true)
            localStore.setUserDataOnRegister(registerUser.copy(password = ""))
        }
        return result
    }

    suspend fun login(user: User): FirebaseResponse {
        val result = remoteDataSource.login(user)
        val userStored = localStore.getUserDataOnRegister()
        if (result.success && localStore.isFirstLogin() && userStored != null) {
            storeUserOnFirestoreIfNotExists(userStored)
        }
        return result
    }

    suspend fun loginWithGoogle(credentials: AuthCredential) : FirebaseResponse{
        val result = remoteDataSource.loginWithGoogle(credentials)
        if(result.success) {
            localStore.setFirstLogin(true)
        }
        return result
    }

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
            localStore.setFirstLogin(false)
            localStore.setUserDataOnRegister(null)
        }
    }

    fun isUserAlreadyLoged() = !localStore.isFirstLogin()

    companion object {
        private const val COLLECTION_USERS = "Users"
    }
}

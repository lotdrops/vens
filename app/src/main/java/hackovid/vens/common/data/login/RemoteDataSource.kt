package hackovid.vens.common.data.login

import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface RemoteDataSource<T> {
    suspend fun login(user: User): T
    suspend fun loginWithGoogle(credentials: AuthCredential): T
    fun isUserAlreadyLoged(): T
    suspend fun registerUser(user: User): T
}

class FirebaseDataSource(
    private val auth: FirebaseAuth,
    private val firebaseErrorMapper: FirebaseErrorMapper
) : RemoteDataSource<FirebaseResponse> {

    override suspend fun login(user: User): FirebaseResponse = withContext(Dispatchers.IO) {
        try {
            auth.signInWithEmailAndPassword(user.email, user.password).await()
            FirebaseResponse(success = true)
        } catch (e: FirebaseException) {
            FirebaseResponse(success = false, error = firebaseErrorMapper.mapToUiError(e))
        }
    }

    override fun isUserAlreadyLoged(): FirebaseResponse {
        return if (auth.currentUser == null) FirebaseResponse(success = false)
        else FirebaseResponse(success = true)
    }

    override suspend fun registerUser(user: User): FirebaseResponse = withContext(Dispatchers.IO) {
        try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            FirebaseResponse(success = true)
        } catch (e: FirebaseException) {
            FirebaseResponse(success = false, error = firebaseErrorMapper.mapToUiError(e))
        }
    }

    override suspend fun loginWithGoogle(credentials: AuthCredential): FirebaseResponse =
        withContext(Dispatchers.IO) {
            try {
                auth.signInWithCredential(credentials).await()
                FirebaseResponse(success = true)
            } catch (e: FirebaseException) {
                FirebaseResponse(success = false, error = firebaseErrorMapper.mapToUiError(e))
            }
        }
}

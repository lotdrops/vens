package hackovid.vens.common.data.login

import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

interface RemoteDataSource<T> {
    suspend fun login(user: User):T
    suspend fun loginWithGoogle(credentials: AuthCredential): T
    fun isUserAlreadyLoged(): T
    suspend fun registerUser(user: User): T
}


class FirebaseDataSource(private val auth: FirebaseAuth, private val firebaseErrorMapper: FirebaseErrorMapper): RemoteDataSource<FireBaseResponse> {

    override suspend fun login(user: User): FireBaseResponse {
        return try {
            auth.signInWithEmailAndPassword(user.email, user.password).await()
            FireBaseResponse(success = true)
        } catch (e : FirebaseException){
            FireBaseResponse(success = false, error = firebaseErrorMapper.mapToUiError(e))
        }
    }

    override fun isUserAlreadyLoged(): FireBaseResponse {
        return if (auth.currentUser == null) FireBaseResponse(success = false)
        else FireBaseResponse(success = true)
    }

    override suspend fun registerUser(user: User): FireBaseResponse {
        return try {
            auth.createUserWithEmailAndPassword(user.email,user.password).await()
            FireBaseResponse(success = true)
        } catch (e : FirebaseException){
            FireBaseResponse(success = false, error = firebaseErrorMapper.mapToUiError(e))
        }
    }

    override suspend fun loginWithGoogle(credentials: AuthCredential): FireBaseResponse {
        return try {
            auth.signInWithCredential(credentials).await()
            FireBaseResponse(success = true)
        } catch (e : FirebaseException){
            FireBaseResponse(success = false, error = firebaseErrorMapper.mapToUiError(e))
        }
    }

}


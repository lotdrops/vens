package hackovid.vens.common.data.login

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onSuccess
import com.github.michaelbull.result.runCatching
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface RemoteDataSource {
    suspend fun login(user: User): Result<Unit, Int>
    suspend fun loginWithGoogle(credentials: AuthCredential): Result<Unit, Int>
    fun isUserAlreadyLoged(): Boolean
    suspend fun registerUser(user: User): Result<Unit, Int>
    suspend fun forgotPassword(email: String): Result<Unit, Int>
}

class FirebaseDataSource(
    private val auth: FirebaseAuth,
    private val firebaseErrorMapper: FirebaseErrorMapper,
    private val db: FirebaseFirestore
) : RemoteDataSource {

    override suspend fun login(user: User) = withContext(Dispatchers.IO) {
        runCatching { auth.signInWithEmailAndPassword(user.email, user.password).await() }
            .andThen { auth ->
                if (auth.isEmailVerified()) Ok(Unit)
                else Err(UserNotVerifiedFirebaseException())
            }.mapError { firebaseErrorMapper.mapToError(it) }
    }

    private fun AuthResult.isEmailVerified() = user?.isEmailVerified == true

    override fun isUserAlreadyLoged() = auth.currentUser != null

    override suspend fun registerUser(user: User) = withContext(Dispatchers.IO) {
        runCatching { auth.createUserWithEmailAndPassword(user.email, user.password).await() }
            .onSuccess { runCatching { it.user?.sendEmailVerification()?.await() } }
            .map { Unit }
            .mapError { firebaseErrorMapper.mapToError(it) }
    }

    override suspend fun loginWithGoogle(credentials: AuthCredential) =
        withContext(Dispatchers.IO) {
            runCatching { auth.signInWithCredential(credentials).await() }
                .map { Unit }
                .mapError { firebaseErrorMapper.mapToError(it) }
        }

    override suspend fun forgotPassword(email: String) = withContext(Dispatchers.IO) {
        runCatching { auth.sendPasswordResetEmail(email).await() }
            .map { Unit }
            .mapError { firebaseErrorMapper.mapToError(it) }
    }
}

package hackovid.vens.common.data.mystore

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onSuccess
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.value.ServerTimestampValue
import com.google.firebase.firestore.model.value.TimestampValue
import com.google.firestore.v1.DocumentTransform
import hackovid.vens.R
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.data.login.User
import hackovid.vens.features.register.RegisterUseCase
import java.time.Instant.now
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UpdateStoreUseCase(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    suspend fun updateStore(store: Store): Result<Unit, Int> = suspendCoroutine { continuation ->
        auth.uid?.let { uid ->
            val storeUpdate = StoreUpdate(store, uid, FieldValue.serverTimestamp())
            db.collection(COLLECTION_STORE_PROPOSALS)
                .add(storeUpdate)
                .addOnSuccessListener { continuation.resume(Ok(Unit)) }
                .addOnFailureListener { continuation.resume(Err(R.string.generic_error_message)) }
        } ?: continuation.resume(Err(R.string.generic_error_message))
    }
}
private const val COLLECTION_STORE_PROPOSALS = "Store Proposals"

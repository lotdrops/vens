package hackovid.vens.common.data.updatedata

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import hackovid.vens.common.data.core.Resource
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UpdateDataDataSource(private val db: FirebaseFirestore) {
    // TODO return a more appropriate type, after mapping
    suspend fun getNewData(sinceDate: Long): Resource<QuerySnapshot> = suspendCoroutine { cont ->
        db.collection(COLLECTION_UPDATED_STORES)
            .whereGreaterThan(FIELD_LAST_MODIFIED, Timestamp(Date(sinceDate)))
            .get()
            .addOnSuccessListener { cont.resume(Resource.Success(it)) }
            .addOnFailureListener { cont.resume(Resource.Failure(it)) }
    }
}
private const val COLLECTION_UPDATED_STORES = "Updated Stores"
private const val FIELD_LAST_MODIFIED = "lastModified"

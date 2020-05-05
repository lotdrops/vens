package hackovid.vens.common.data.updatedata

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.core.Resource
import hackovid.vens.common.data.json.toStoreSubtype
import hackovid.vens.common.data.json.toStoreType
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UpdateDataDataSource(private val db: FirebaseFirestore) {
    suspend fun getNewData(sinceDate: Long): Resource<List<StoreUpdateModel>> =
        suspendCoroutine { continuation ->
            db.collection(COLLECTION_UPDATED_STORES)
                .whereGreaterThan(FIELD_LAST_MODIFIED, Timestamp(Date(sinceDate)))
                .get()
                .addOnSuccessListener { continuation.resume(Resource.Success(it.mapToUpdates())) }
                .addOnFailureListener { continuation.resume(Resource.Failure(it)) }
        }

    private fun QuerySnapshot.mapToUpdates() = this.mapNotNull { document ->
        val changeType: Long? = document.get("changeType") as? Long
        val lastModified = (document.get("lastModified") as? Timestamp)?.toDate()?.time
        val storeId = document.get("id") as? Long
        val store = document.get("store", StoreDto::class.java)?.toStore(storeId ?: 0)
        when {
            (storeId == null || lastModified == null) -> null
            changeType == -1L -> StoreUpdateModel.Removed(storeId, lastModified)
            changeType == 0L && store != null -> StoreUpdateModel.Updated(lastModified, store)
            changeType == 1L && store != null -> StoreUpdateModel.Added(lastModified, store)
            else -> null
        }
    }

    private fun StoreDto.toStore(id: Long) =
        if (name == null || location == null || type == null || subtype == null) null
        else Store(
            id = id,
            latitude = location.latitude,
            longitude = location.longitude,
            name = name,
            type = type.toStoreType(),
            subtype = subtype.toStoreSubtype(),
            phone = phone,
            mobilePhone = mobilePhone,
            address = address,
            web = web,
            email = email,
            schedule = schedule,
            acceptsOrders = acceptsOrders,
            delivers = delivers
        )
}
private const val COLLECTION_UPDATED_STORES = "Updated Stores"
private const val FIELD_LAST_MODIFIED = "lastModified"

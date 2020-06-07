package hackovid.vens.common.data.mystore

import com.google.firebase.firestore.FieldValue
import hackovid.vens.common.data.Store

data class StoreUpdate(
    val store: Store,
    val uid: String,
    val updateTime: FieldValue
)
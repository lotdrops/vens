package hackovid.vens.common.data.updatedata

import com.google.firebase.firestore.GeoPoint

data class StoreDto(
    val name: String? = null,
    val location: GeoPoint? = null,
    val type: String? = null,
    val subtype: String? = null,
    val address: String? = null,
    val email: String? = null,
    val web: String? = null,
    val phone: String? = null,
    val mobilePhone: String? = null,
    val schedule: String? = null,
    val acceptsOrders: Boolean? = null,
    val delivers: Boolean? = null
)

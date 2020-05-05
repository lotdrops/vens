package hackovid.vens.common.ui

import android.location.Location
import hackovid.vens.common.data.StoreAndFavourite
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.data.core.distance
import kotlin.math.roundToInt

data class StoreListUi(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val type: StoreType,
    val subtype: StoreSubtype,
    val isFavourite: Boolean = false,
    val phone: String? = null,
    val mobilePhone: String? = null,
    val address: String? = null,
    val web: String? = null,
    val email: String? = null,
    val crowd: Int? = null,
    val distance: Int? = null,
    val acceptsOrders: Boolean? = null,
    val delivers: Boolean? = null
)

fun StoreAndFavourite.toListUi(location: Location?) = StoreListUi(
    id = store.id,
    latitude = store.latitude,
    longitude = store.longitude,
    name = store.name,
    type = store.type,
    subtype = store.subtype,
    isFavourite = userStore != null,
    phone = store.phone,
    mobilePhone = store.mobilePhone,
    address = store.address,
    web = store.web,
    email = store.email,
    crowd = null,
    distance = location?.distance(store.latitude, store.longitude)?.roundToInt(),
    acceptsOrders = store.acceptsOrders,
    delivers = store.delivers
)

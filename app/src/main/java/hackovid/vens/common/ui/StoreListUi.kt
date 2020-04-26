package hackovid.vens.common.ui

import android.location.Location
import hackovid.vens.common.data.Store
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

fun Store.toListUi(location: Location?) = StoreListUi(
    id = id,
    latitude = latitude,
    longitude = longitude,
    name = name,
    type = type,
    subtype = subtype,
    isFavourite = isFavourite,
    phone = phone,
    mobilePhone = mobilePhone,
    address = address,
    web = web,
    email = email,
    crowd = null,
    distance = location?.distance(latitude, longitude)?.roundToInt(),
    acceptsOrders = acceptsOrders,
    delivers = delivers
)

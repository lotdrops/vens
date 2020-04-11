package hackovid.vens.common.data.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreType
import kotlin.random.Random

@JsonClass(generateAdapter = true)
data class RemoteStore(
    val id: Long = 0,
    @Json(name = "lat") val latitude: Double,
    @Json(name = "long") val longitude: Double,
    val name: String,
    val type: String,
    val adress: String
)

fun RemoteStore.toStore() = Store(
    id = id,
    latitude = latitude,
    longitude = longitude,
    name = name,
    type = StoreType.values()[Random.nextInt(StoreType.values().size)],
    isFavourite = false,
    phone = null,
    mobilePhone = null,
    address = null,
    web = null,
    email = null,
    schedule = null,
    acceptsOrders = null,
    delivers = null)

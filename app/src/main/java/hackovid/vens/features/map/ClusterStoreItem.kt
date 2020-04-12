package hackovid.vens.features.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import kotlin.math.roundToInt

data class ClusterStoreItem(
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val latLong: LatLng = LatLng(latitude, longitude),
    val name: String,
    val type: StoreType,
    val subtype: StoreSubtype,
    val isFavourite: Boolean = false,
    val phone: String? = null,
    val mobilePhone: String? = null,
    val address: String? = null,
    val web: String? = null,
    val email: String? = null,
    val schedule: String? = null,
    val crowd: Int? = null,
    val distance: Int? = null,
    val acceptsOrders: Boolean? = null,
    val delivers: Boolean? = null
) : ClusterItem {

    override fun getSnippet(): String {
        return ""
    }

    override fun getTitle(): String {
        return name
    }

    override fun getPosition(): LatLng {
        return latLong
    }
}

fun Store.toClusterStoreItem(location: Location?) = ClusterStoreItem(
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
    schedule = schedule,
    crowd = null,
    distance = location?.distance(latitude, longitude),
    acceptsOrders = acceptsOrders,
    delivers = delivers)

private fun Location.distance(latitude: Double, longitude: Double) = Location("").apply {
    setLatitude(latitude)
    setLongitude(longitude)
}.distanceTo(this).roundToInt()

package hackovid.vens.features.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import hackovid.vens.common.data.StoreAndFavourite
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.data.core.distance
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

fun StoreAndFavourite.toClusterStoreItem(location: Location?) = ClusterStoreItem(
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
    schedule = store.schedule,
    crowd = null,
    distance = location?.distance(store.latitude, store.longitude)?.roundToInt(),
    acceptsOrders = store.acceptsOrders,
    delivers = store.delivers)

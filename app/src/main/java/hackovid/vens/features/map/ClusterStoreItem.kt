package hackovid.vens.features.map

import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.data.core.Converters

data class ClusterStoreItem(
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val latLong:LatLng = LatLng(latitude, longitude),
    val name: String,
    @TypeConverters(Converters::class) val type: StoreType,
    val isFavourite: Boolean = false,
    val phone: String? = null,
    val mobilePhone: String? = null,
    val address: String? = null,
    val web: String? = null,
    val email: String? = null,
    val schedule: String? = null,
    val acceptsOrders: Boolean? = null,
    val delivers: Boolean? = null
): ClusterItem {

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




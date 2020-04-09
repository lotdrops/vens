package hackovid.vens.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hackovid.vens.common.data.core.Converters

@Entity(tableName = "Stores")
data class Store(
    @PrimaryKey val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
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
)

enum class StoreType {
    SUPERMARKET,
    GREENGROCERY,
    BUTCHER_SHOP,
    UNKNOWN
}

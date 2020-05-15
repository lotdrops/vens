package hackovid.vens.common.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Favourites")
data class Favourite(
    @PrimaryKey
    @ForeignKey(entity = Store::class,
        onDelete = CASCADE,
        onUpdate = CASCADE,
        childColumns = ["storeId"],
        parentColumns = ["id"])
    val storeId: Long
)

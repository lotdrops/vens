package hackovid.vens.common.data

import androidx.room.Embedded
import androidx.room.Relation

data class StoreAndFavourite(
    @Embedded val store: Store,
    @Relation(
        parentColumn = "id",
        entityColumn = "storeId"
    )
    val userStore: Favourite?
)

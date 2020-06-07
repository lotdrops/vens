package hackovid.vens.common.data.mystore

import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.data.core.Converters

data class StoreUpdate(
    val store: Store,
    val uid: String,
    val updateTime: Long
)
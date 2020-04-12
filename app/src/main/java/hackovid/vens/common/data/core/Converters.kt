package hackovid.vens.common.data.core

import androidx.room.TypeConverter
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType

class Converters {
    @TypeConverter
    fun storeTypeToInt(storeType: StoreType): Int = storeType.ordinal

    @TypeConverter
    fun intToStoreType(ordinalOfStoreType: Int): StoreType = StoreType.values()[ordinalOfStoreType]

    @TypeConverter
    fun storeSubtypeToInt(storeSubtype: StoreSubtype): Int = storeSubtype.ordinal

    @TypeConverter
    fun intToStoreSubtype(ordinalOfStoreSubtype: Int): StoreSubtype =
        StoreSubtype.values()[ordinalOfStoreSubtype]
}

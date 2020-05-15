package hackovid.vens.common.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import hackovid.vens.common.data.core.BaseDao

@Dao
abstract class FavouriteDao : BaseDao<Favourite>() {

    @Insert
    abstract suspend fun setFavourite(favorite: Favourite)

    @Delete
    abstract suspend fun removeFavourite(favorite: Favourite)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addFavourite(favorite: Favourite)
}

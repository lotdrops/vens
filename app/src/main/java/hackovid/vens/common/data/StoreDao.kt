package hackovid.vens.common.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import hackovid.vens.common.data.core.BaseDao

@Dao
abstract class StoreDao : BaseDao<Store>() {
    @Query("DELETE FROM Stores WHERE id = :id")
    abstract suspend fun deleteStore(id: Long)

    @Query("SELECT * FROM Stores ORDER BY name LIMIT(:limit)")
    abstract fun getAllByName(limit: Int = -1): LiveData<List<Store>>

    @Query("SELECT * FROM Stores WHERE isFavourite ORDER BY name")
    abstract fun getFavouritesByName(): LiveData<List<Store>>

    @Query("SELECT * FROM Stores WHERE id = :storeId")
    abstract fun getStoreById(storeId: Int): LiveData<Store>

    @Query("UPDATE Stores SET isFavourite = :newIsFavourite WHERE id = :id")
    abstract suspend fun setFavourite(id: Long, newIsFavourite: Boolean)

    @RawQuery(observedEntities = [Store::class])
    abstract fun getByQuery(query: SupportSQLiteQuery): LiveData<List<Store>>

    @Query("SELECT * FROM Stores")
    abstract suspend fun getAll(): List<Store>

    @Query("SELECT * FROM Stores")
    abstract fun getAllStores(): LiveData<List<Store>>
}

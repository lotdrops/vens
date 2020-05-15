package hackovid.vens.common.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import hackovid.vens.common.data.core.BaseDao

@Dao
abstract class StoreDao : BaseDao<Store>() {
    @Query("DELETE FROM Stores WHERE id = :id")
    abstract suspend fun deleteStore(id: Long)

    @Transaction
    @Query("SELECT * FROM Stores ORDER BY name LIMIT(:limit)")
    abstract fun getAllByName(limit: Int = -1): LiveData<List<StoreAndFavourite>>

    @Transaction
    @Query("SELECT * FROM Stores INNER JOIN Favourites ON Favourites.storeId = Stores.id ORDER BY name")
    abstract fun getFavouritesByName(): LiveData<List<StoreAndFavourite>>

    @Transaction
    @Query("SELECT * FROM Stores WHERE id = :storeId")
    abstract fun getStoreById(storeId: Int): LiveData<StoreAndFavourite>

    @Query("SELECT * FROM Stores WHERE id = :storeId")
    abstract suspend fun getStoreByIdSuspend(storeId: Long): Store

    @Query("SELECT * FROM Stores WHERE name LIKE :name")
    abstract fun getStoreByName(name: String): LiveData<List<Store>>

    @RawQuery(observedEntities = [Store::class])
    abstract fun getByQuery(query: SupportSQLiteQuery): LiveData<List<StoreAndFavourite>>

    @Transaction
    @Query("SELECT * FROM Stores")
    abstract suspend fun getAll(): List<StoreAndFavourite>

    @Transaction
    @Query("SELECT * FROM Stores")
    abstract fun getAllStores(): LiveData<List<StoreAndFavourite>>
}

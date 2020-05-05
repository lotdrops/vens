package hackovid.vens.common.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import hackovid.vens.common.data.core.BaseDao

@Dao
interface StoreDao : BaseDao<Store> {
    @Query("SELECT * FROM Stores ORDER BY name LIMIT(:limit)")
    fun getAllByName(limit: Int = -1): LiveData<List<Store>>

    @Query("SELECT * FROM Stores WHERE isFavourite ORDER BY name")
    fun getFavouritesByName(): LiveData<List<Store>>

    @Query("SELECT * FROM Stores WHERE id = :storeId")
    fun getStoreById(storeId: Int): LiveData<Store>

    @Query("SELECT * FROM Stores WHERE name LIKE :name")
    fun getStoreByName(name: String): LiveData<List<Store>>

    @Query("UPDATE Stores SET isFavourite = :newIsFavourite WHERE id = :id")
    suspend fun setFavourite(id: Long, newIsFavourite: Boolean)

    @RawQuery(observedEntities = [Store::class])
    fun getByQuery(query: SupportSQLiteQuery): LiveData<List<Store>>

    @Query("SELECT * FROM Stores")
    suspend fun getAll(): List<Store>

    @Query("SELECT * FROM Stores")
    fun getAllStores(): LiveData<List<Store>>
}

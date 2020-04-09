package hackovid.vens.common.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import hackovid.vens.common.data.core.BaseDao

@Dao
interface StoreDao : BaseDao<Store> {
    @Query("SELECT * FROM Stores ORDER BY name")
    fun getAllByName(): LiveData<List<Store>>

    @Query("SELECT * FROM Stores WHERE isFavourite ORDER BY name")
    fun getFavouritesByName(): LiveData<List<Store>>

    @Query("SELECT * FROM Stores WHERE id = :storeId")
    fun getStoreById(storeId: Int): LiveData<Store>

    @Query("UPDATE Stores SET isFavourite = :newIsFavourite WHERE id = :id")
    suspend fun setFavourite(id: Long, newIsFavourite: Boolean)
}

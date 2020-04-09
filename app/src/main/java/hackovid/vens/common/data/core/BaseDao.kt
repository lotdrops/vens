package hackovid.vens.common.data.core

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    fun insert(obj: T): Long

    @Insert
    fun insertList(obj: List<T>)

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}

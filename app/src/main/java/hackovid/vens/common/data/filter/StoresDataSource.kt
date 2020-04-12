package hackovid.vens.common.data.filter

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao

class StoresDataSource(private val favouritesOnly: Boolean, private val storeDao: StoreDao) {
    fun getData(params: Pair<FilterParams, Location?>?): LiveData<List<Store>> {
        params?.let { notNullParams ->
            if (notNullParams.second != null) {
                // TODO sort by distance
            } else {
                val query = "SELECT * FROM Stores${buildWhereClause(notNullParams)} ORDER BY name"
                return storeDao.getByQuery(SimpleSQLiteQuery(query))
            }
        }
        return MutableLiveData()
    }

    private fun buildWhereClause(params: Pair<FilterParams, Location?>): String {
        val favsClause = if (favouritesOnly) " WHERE isFavourite" else ""
        val catCondition = params.first.categories.categoriesInCondition()
        val beforeCatClause = if (favouritesOnly && catCondition.isNotEmpty()) " AND "
        else if (catCondition.isNotEmpty()) " WHERE " else ""

        return "$favsClause$beforeCatClause $catCondition"
    }

    private fun List<Boolean>.categoriesInCondition(): String {
        var query = ""
        val filterOut = this.mapIndexed { i, selected ->
            if (!selected) {
                i
            } else {
                null
            }
        }.filterNotNull()
        if (filterOut.isNotEmpty()) {
            query += " (type NOT IN (${filterOut.joinToString { it.toString() }}))"
        }
        return query
    }
}

package hackovid.vens.common.data.filter

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao
import java.lang.Math.PI
import kotlin.math.cos

class StoresDataSource(private val favouritesOnly: Boolean, private val storeDao: StoreDao) {
    fun getData(params: Pair<FilterParams, Location?>?): LiveData<List<Store>> {
        // TODO return appropriate but without sorting
        params?.let { notNullParams ->
            val orderCriteria = if (notNullParams.second != null) {
                buildOrderByDistance(notNullParams)
            } else {
                "ORDER BY name"
            }
            val query = "SELECT * FROM Stores${buildWhereClause(notNullParams)} $orderCriteria"
            return storeDao.getByQuery(SimpleSQLiteQuery(query))
        }
        return MutableLiveData()
    }

    fun getDataByDistance(params: Pair<FilterParams, Location>): LiveData<List<Store>> {
        val orderCriteria = buildOrderByDistance(params)
        val distanceFilter = buildDistanceFilter(params)
        val query = "SELECT * FROM Stores ${buildWhereClause(params)} $distanceFilter $orderCriteria"
        return storeDao.getByQuery(SimpleSQLiteQuery(query))
    }

    private fun buildDistanceFilter(params: Pair<FilterParams, Location>): String {
        val latitude = params.second.latitude
        val longitude = params.second.longitude
        val distance = params.first.distance
        val longitudeEast = longitude + (distance / EARTH_RADIUS) * (180 /PI) / cos(latitude * PI / 180)
        val longitudeWest = longitude - ((distance / EARTH_RADIUS) * (180 / PI) / cos(latitude * PI / 180))
        val latitudeNorth = latitude + (distance/ EARTH_RADIUS) * 180 / PI
        val latitudeSouth = latitude - ((distance/ EARTH_RADIUS) * 180 / PI)
        
        return " AND (latitude BETWEEN $latitudeSouth AND $latitudeNorth) AND (longitude BETWEEN $longitudeWest AND $longitudeEast)"
    }

    fun getDataByName(
        params: Pair<FilterParams, Location?>?,
        limit: Int? = null
    ): LiveData<List<Store>> {
        params?.let { notNullParams ->
            val orderCriteria = "ORDER BY name"
            val query = "SELECT * FROM Stores $orderCriteria"
            return storeDao.getByQuery(SimpleSQLiteQuery(query))
        }
        return MutableLiveData()
    }

    private fun buildOrderByDistance(params: Pair<FilterParams, Location?>): String {
        return "ORDER BY ((longitude -(${params.second?.longitude})) *" +
                "(longitude -(${params.second?.longitude}))) + ((latitude -(${params.second?.latitude})) " +
                "* (latitude -(${params.second?.latitude})))"
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

private const val EARTH_RADIUS = 6378000.0

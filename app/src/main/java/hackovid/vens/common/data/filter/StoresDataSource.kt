package hackovid.vens.common.data.filter

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreAndFavourite
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.updatedata.UpdateStoresDataSource
import java.lang.Math.PI
import kotlin.math.cos

class StoresDataSource(
    private val storeDao: StoreDao
) : UpdateStoresDataSource {
    override suspend fun deleteStore(id: Long) = storeDao.deleteStore(id)
    override suspend fun upsertStore(store: Store) = storeDao.upsert(store)

    fun getUnorderedData(params: Pair<FilterParams, Location?>?): LiveData<List<StoreAndFavourite>> {
        return if (params != null) {
            val whereClause = buildWhereClause(params)
            val distanceFilter = buildDistanceFilter(params)
            buildQuery(whereClause, distanceFilter)
        } else {
            storeDao.getAllStores()
        }
    }

    fun getDataByDistance(
        params: Pair<FilterParams, Location>,
        limit: Int = -1
    ): LiveData<List<StoreAndFavourite>> {
        val whereClause = buildWhereClause(params)
        val distanceFilter = buildDistanceFilter(params)
        val orderCriteria = buildOrderByDistance(params)
        return buildQuery(whereClause, distanceFilter, orderCriteria, limit)
    }

    fun getDataByName(
        params: Pair<FilterParams, Location?>,
        limit: Int = -1
    ): LiveData<List<StoreAndFavourite>> {
        val whereClause = buildWhereClause(params)
        val distanceFilter = buildDistanceFilter(params)
        val orderCriteria = "ORDER BY name"
        return buildQuery(whereClause, distanceFilter, orderCriteria, limit)
    }

    fun getDataByName(limit: Int = -1) = storeDao.getAllByName(limit)

    private fun buildQuery(
        whereClause: String,
        distanceFilter: String,
        orderCriteria: String = "",
        limit: Int = -1
    ): LiveData<List<StoreAndFavourite>> {
        val query =
            "SELECT * from STORES $whereClause ${applyDistanceFilter(whereClause, distanceFilter)}"
        return getByQuery("$query $orderCriteria LIMIT($limit)")
    }
    private fun getByQuery(query: String) = storeDao.getByQuery(SimpleSQLiteQuery(query))

    private fun buildDistanceFilter(params: Pair<FilterParams, Location?>): String {
        return if (shouldBuildDistanceFilter(params))
            calculateDistanceFilter(params.first.distance, params.second as Location)
        else ""
    }

    private fun shouldBuildDistanceFilter(params: Pair<FilterParams, Location?>) =
        params.second != null && params.first.distance != FilterParams.ANY_DISTANCE

    private fun applyDistanceFilter(
        whereClause: String,
        distanceFilter: String
    ): String {
        if (whereClause != " ") {
            if (distanceFilter.isNotEmpty())
                return "AND $distanceFilter"
        } else {
            if (distanceFilter.isNotEmpty())
                return "WHERE $distanceFilter"
        }
        return ""
    }

    private fun calculateDistanceFilter(distanceType: Int, location: Location): String {
        val latitude = location.latitude
        val longitude = location.longitude
        val distance = getDistanceFromType(distanceType)
        val longitudeEast = longitude + (distance / EARTH_RADIUS) * (180 / PI) / cos(latitude * PI / 180)
        val longitudeWest = longitude - ((distance / EARTH_RADIUS) * (180 / PI) / cos(latitude * PI / 180))
        val latitudeNorth = latitude + (distance / EARTH_RADIUS) * 180 / PI
        val latitudeSouth = latitude - ((distance / EARTH_RADIUS) * 180 / PI)

        return "(latitude BETWEEN $latitudeSouth AND $latitudeNorth) AND (longitude BETWEEN $longitudeWest AND $longitudeEast)"
    }

    private fun getDistanceFromType(distanceType: Int) = when (distanceType) {
        FilterParams.MEDIUM_DISTANCE -> MEDIUM_DISTANCE_METERS * MULTIPLIER
        else -> SHORT_DISTANCE_METERS * MULTIPLIER
    }

    private fun buildOrderByDistance(params: Pair<FilterParams, Location?>): String {
        return "ORDER BY ((longitude -(${params.second?.longitude})) *" +
                "(longitude -(${params.second?.longitude}))) + ((latitude -(${params.second?.latitude})) " +
                "* (latitude -(${params.second?.latitude})))"
    }

    private fun buildWhereClause(params: Pair<FilterParams, Location?>): String {
        val favsClause = if (params.first.favouritesOnly) {
            " INNER JOIN Favourites ON Favourites.storeId = Stores.id"
        } else ""
        val catCondition = params.first.categories.categoriesInCondition()
        val beforeCatClause = if (params.first.favouritesOnly && catCondition.isNotEmpty()) " AND "
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
            query += " type NOT IN (${filterOut.joinToString { it.toString() }})"
        }
        return query
    }
}

private const val EARTH_RADIUS = 6378000.0
private const val MULTIPLIER = 1.1

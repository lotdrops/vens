package hackovid.vens.common.data.updatedata

import com.google.firebase.firestore.QuerySnapshot
import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.core.Resource
import hackovid.vens.common.data.filter.StoresDataSource
import java.util.concurrent.TimeUnit

class UpdateDataUseCase(
    private val dataSource: UpdateDataDataSource,
    private val storesDataSource: StoresDataSource,
    private val localStorage: LocalStorage
) {
    suspend fun updateData() {
        val lastUpdate = localStorage.getLastUpdateTimestamp()
        if (shouldUpdate(lastUpdate)) {
            val newDataResult = dataSource.getNewData(lastUpdate)
            val updateTimestamp = newDataResult.getNewUpdateTimestamp()
            if (updateTimestamp != null) {
                localStorage.setLastUpdateTimestamp(updateTimestamp)
            }
            newDataResult.updateStoresData()
        }
    }

    private fun shouldUpdate(lastUpdate: Long): Boolean =
        System.currentTimeMillis() >= lastUpdate + TimeUnit.HOURS.toMillis(HOURS_TO_UPDATE_DATE)

    private fun Resource<QuerySnapshot>.getNewUpdateTimestamp(): Long? {
        // TODO get timestamp if not empty
        return null
    }

    private fun Resource<QuerySnapshot>.updateStoresData() {
        // TODO save store changes
    }
}
private const val HOURS_TO_UPDATE_DATE = 6L

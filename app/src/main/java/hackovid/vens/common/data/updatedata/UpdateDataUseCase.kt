package hackovid.vens.common.data.updatedata

import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.config.RemoteConfig
import hackovid.vens.common.data.core.Resource
import java.util.concurrent.TimeUnit

class UpdateDataUseCase(
    private val remoteDataSource: UpdateDataDataSource,
    private val storesDataSource: UpdateStoresDataSource,
    private val localStorage: LocalStorage,
    private val remoteConfig: RemoteConfig
) {
    private val timeBetweenUpdates: Long
        get() = TimeUnit.HOURS.toMillis(remoteConfig.hoursIntervalRefreshStores)

    suspend fun updateData() {
        val lastUpdate = localStorage.getLastUpdateTimestamp()
        if (shouldUpdate(lastUpdate)) {
            val newDataResult = remoteDataSource.getNewData(lastUpdate)
            val updateTimestamp = newDataResult.getNewUpdateTimestamp()
            if (updateTimestamp != null) {
                localStorage.setLastUpdateTimestamp(updateTimestamp)
            }
            (newDataResult as? Resource.Success)?.data?.clean()?.updateStoresData()
        }
    }

    private fun shouldUpdate(lastUpdate: Long): Boolean =
        System.currentTimeMillis() >= lastUpdate + timeBetweenUpdates

    private fun List<StoreUpdateModel>.clean(): List<StoreUpdateModel> = this
        .groupBy { it.id }
        .flatMap { it.value.removeStoreRedundant() }

    private fun List<StoreUpdateModel>.removeStoreRedundant(): List<StoreUpdateModel> = when {
        this.hasRemovedAndAdded() -> emptyList()
        this.hasRemoved() -> listOf(this.first { it is StoreUpdateModel.Removed })
        else -> this.toLatest()
    }

    private fun List<StoreUpdateModel>.hasRemoved() = this.any { it is StoreUpdateModel.Removed }
    private fun List<StoreUpdateModel>.hasAdded() = this.any { it is StoreUpdateModel.Added }
    private fun List<StoreUpdateModel>.hasRemovedAndAdded() = this.hasRemoved() && this.hasAdded()

    private fun List<StoreUpdateModel>.toLatest() = listOfNotNull(maxBy { it.lastModified })

    private fun Resource<List<StoreUpdateModel>>.getNewUpdateTimestamp() =
        (this as? Resource.Success)?.data?.maxBy { it.lastModified }?.lastModified

    private suspend fun List<StoreUpdateModel>.updateStoresData() = forEach { storeUpdate ->
        when (storeUpdate) {
            is StoreUpdateModel.Removed -> storesDataSource.deleteStore(storeUpdate.id)
            is StoreUpdateModel.Updated -> storesDataSource.upsertStore(storeUpdate.store)
            is StoreUpdateModel.Added -> storesDataSource.upsertStore(storeUpdate.store)
        }
    }
}

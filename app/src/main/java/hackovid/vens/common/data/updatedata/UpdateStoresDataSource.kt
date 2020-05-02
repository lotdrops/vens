package hackovid.vens.common.data.updatedata

import hackovid.vens.common.data.Store

interface UpdateStoresDataSource {
    suspend fun deleteStore(id: Long)
    suspend fun upsertStore(store: Store)
}

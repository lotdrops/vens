package hackovid.vens.common.data.updatedata

import hackovid.vens.common.data.Store

class UpdateStoresDataSourceMock : UpdateStoresDataSource {
    val stores = mutableListOf<Store>()

    override suspend fun deleteStore(id: Long) {
        stores.removeIf { it.id == id }
    }

    override suspend fun upsertStore(store: Store) {
        deleteStore(store.id)
        stores.add(store)
    }
}

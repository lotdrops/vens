package hackovid.vens.common.data.updatedata

import hackovid.vens.common.data.Store

sealed class StoreUpdateModel {
    abstract val lastModified: Long
    abstract val id: Long

    data class Removed(override val id: Long, override val lastModified: Long) : StoreUpdateModel()
    data class Added(
        override val lastModified: Long,
        val store: Store
    ) : StoreUpdateModel() {
        override val id: Long = store.id
    }
    data class Updated(
        override val lastModified: Long,
        val store: Store
    ) : StoreUpdateModel() {
        override val id: Long = store.id
    }
}

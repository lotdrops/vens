package hackovid.vens.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import hackovid.vens.R
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.StoreType

class DetailViewModel(storeDao: StoreDao, storeId: Int) : ViewModel() {
    val store = storeDao.getStoreById(storeId)
    val image = store.map { it.type.imageDetail() }

    private fun StoreType.imageDetail() : Int {
        return when (store.value?.type) {
            StoreType.GROCERY -> R.drawable.grocery
            StoreType.BEAUTY -> R.drawable.beauty
            StoreType.MALL -> R.drawable.mall
            StoreType.GALLERY -> R.drawable.mall
            StoreType.HOME -> R.drawable.home
            StoreType.MARKET -> R.drawable.market
            StoreType.FASHION -> R.drawable.fashion
            StoreType.LEISURE -> R.drawable.leisure
            StoreType.EVERYDAY -> R.drawable.everyday
            StoreType.SERVICES -> R.drawable.services
            StoreType.OTHER -> R.drawable.other
            null -> R.drawable.grocery
        }
    }
}

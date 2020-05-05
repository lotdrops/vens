package hackovid.vens.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import hackovid.vens.R
import hackovid.vens.common.data.Favourite
import hackovid.vens.common.data.FavouriteDao
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.toListUi
import hackovid.vens.common.ui.toLocation
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.common.utils.combineWith
import kotlinx.coroutines.launch

class DetailViewModel(
    sharedViewModel: SharedViewModel,
    private val storeDao: StoreDao,
    private val favouriteDao: FavouriteDao,
    storeId: Int
) : ViewModel() {
    private val store = storeDao.getStoreById(storeId)
    val storeUi = sharedViewModel.location.combineWith(store) { latLng, store ->
        store?.toListUi(latLng.toLocation())
    }
    val image = store.map { it.store.type.imageDetail() }

    val backEvent = SingleLiveEvent<Unit>()

    fun onFavouriteClicked() {
        viewModelScope.launch {
            val id = storeUi.value?.id
            val isFavourite = storeUi.value?.isFavourite
            if (id != null && isFavourite != null) {
                val fav = Favourite(id)
                if(isFavourite) favouriteDao.removeFavourite(fav) else favouriteDao.addFavourite(fav)
            }
        }
    }

    fun onBackClicked() {
        backEvent.call()
    }

    private fun StoreType.imageDetail(): Int {
        return when (store.value?.store?.type) {
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

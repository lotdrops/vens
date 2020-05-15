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
                if (isFavourite) favouriteDao.removeFavourite(fav) else favouriteDao.addFavourite(fav)
            }
        }
    }

    fun onBackClicked() {
        backEvent.call()
    }

    private fun StoreType.imageDetail(): Int {
        return when (store.value?.store?.type) {
            StoreType.BAKERY_PASTRY_DAIRY -> R.drawable.bakery
            StoreType.DRINKS -> R.drawable.drinks
            StoreType.EGGS_AND_BIRDS -> R.drawable.eggs_birds
            StoreType.FASHION -> R.drawable.fashion
            StoreType.FISH_AND_SEA_FOOD -> R.drawable.fish_sea_food
            StoreType.FOOD -> R.drawable.food
            StoreType.FRUIT_AND_VEGETABLES -> R.drawable.fruits_and_vegetables
            StoreType.HEALTH -> R.drawable.health
            StoreType.HOME -> R.drawable.home
            StoreType.LEISURE_AND_CULTURE -> R.drawable.leisure_culture
            StoreType.LOOK -> R.drawable.look
            StoreType.MARKET -> R.drawable.market
            StoreType.MEAT -> R.drawable.meat
            StoreType.OTHER -> R.drawable.others
            StoreType.PREPARED_DISHES -> R.drawable.prepared_dishes
            StoreType.SERVICES -> R.drawable.services
            StoreType.SHOPPING_CENTER -> R.drawable.comercial_gallery
            StoreType.SHOPPING_GALLERY -> R.drawable.comercial_gallery
            StoreType.SUPERMARKET -> R.drawable.supermarket
            else -> R.drawable.others
        }
    }
}

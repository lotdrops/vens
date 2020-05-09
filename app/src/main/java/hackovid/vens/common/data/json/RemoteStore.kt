package hackovid.vens.common.data.json

import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType

@JsonClass(generateAdapter = true)
data class RemoteStore(
    @Json(name = "id") val id: Long = 0,
    @Json(name = "lat") val latitude: Double,
    @Json(name = "long") val longitude: Double,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "subtype") val subtype: String,
    @Json(name = "adress") val adress: String?,
    @Json(name = "web") val web: String?
)

fun RemoteStore.toStore() = Store(
    id = id,
    latitude = latitude,
    longitude = longitude,
    name = name,
    type = type.toStoreType(),
    subtype = subtype.toStoreSubtype(),
    phone = null,
    mobilePhone = null,
    address = adress,
    web = web,
    email = null,
    schedule = null,
    acceptsOrders = null,
    delivers = null
)

fun String.toStoreType() = when (this.toLowerCase()) {
    "bakery, pastry and dairy" -> StoreType.BAKERY_PASTRY_DAIRY
    "drinks" -> StoreType.DRINKS
    "eggs and birds" -> StoreType.EGGS_AND_BIRDS
    "fashion" -> StoreType.FASHION
    "fish and seafood" -> StoreType.FISH_AND_SEA_FOOD
    "food" -> StoreType.FOOD
    "fruits and vegetables" -> StoreType.FASHION
    "health" -> StoreType.HEALTH
    "home" -> StoreType.HOME
    "leisure and culture" -> StoreType.LEISURE_AND_CULTURE
    "look" -> StoreType.LOOK
    "market" -> StoreType.MARKET
    "meat" -> StoreType.MEAT
    "others" -> StoreType.OTHER
    "prepared dishes" -> StoreType.PREPARED_DISHES
    "services" -> StoreType.SERVICES
    "shopping centre" -> StoreType.SHOPPING_CENTER
    "shopping gallery" -> StoreType.SHOPPING_GALLERY
    "supermarket" -> StoreType.SUPERMARKET
    else -> StoreType.OTHER.also {
        Log.d("Parse json", "Type (${this@toStoreType}) not classified!")
    }
}

fun String.toStoreSubtype() = when (this.toLowerCase().replace("\n","")) {
    "bakery, pastry and dairy".toLowerCase() -> StoreSubtype.BAKERY_PASTRY_DAIRY
    "drinks".toLowerCase() -> StoreSubtype.DRINKS
    "eggs and birds".toLowerCase() -> StoreSubtype.EGGS_AND_BIRDS
    "dress up".toLowerCase() -> StoreSubtype.DRESS
    "footwear and leather".toLowerCase() -> StoreSubtype.FOOTWEAR_AND_LEATHER
    "haberdashery".toLowerCase() -> StoreSubtype.HABERDASHERY
    "jewelry and watches".toLowerCase() -> StoreSubtype.JEWLERY_AND_WATCHES
    "repairs".toLowerCase() -> StoreSubtype.REPAIRS
    "fish and seafood".toLowerCase() -> StoreSubtype.FISH_AND_SEAFOOD
    "others".toLowerCase() -> StoreSubtype.OTHERS
    "fruits and vegetables".toLowerCase() -> StoreSubtype.FRUITS_VEGGIES
    "health and care".toLowerCase() -> StoreSubtype.HEALTH_AND_CARE
    "herbalists, dietetics and nutrition".toLowerCase() -> StoreSubtype.DIETETICS_NUTRITION
    "pharmacy and parapharmacy".toLowerCase() -> StoreSubtype.PHARMACY_AND_PHARAFARMACY
    "appliances".toLowerCase() -> StoreSubtype.APPLIANCES
    "equipment".toLowerCase() -> StoreSubtype.EQUIPMENT
    "flower shop".toLowerCase() -> StoreSubtype.FLOWER_SHOP
    "furniture and articles".toLowerCase() -> StoreSubtype.FURNITURE_AND_ARTICLES
    "hardware store".toLowerCase() -> StoreSubtype.HARDWARE
    "stamps, coins and antiques".toLowerCase() -> StoreSubtype.STAMPS_COINS_ANTIQUES
    "bookshop, newspapers and magazines".toLowerCase() -> StoreSubtype.BOOKSHOP_NEWSPAPER_MAGAZINES
    "computers".toLowerCase() -> StoreSubtype.COMPUTER_STORE
    "music".toLowerCase() -> StoreSubtype.MUSIC
    "toys and sports".toLowerCase() -> StoreSubtype.TOYS_AND_SPORTS
    "beauty center".toLowerCase() -> StoreSubtype.BEAUTY_CENTER
    "hair salon".toLowerCase() -> StoreSubtype.HAIR_SALON
    "market".toLowerCase() -> StoreSubtype.MARKET
    "meat".toLowerCase() -> StoreSubtype.MEAT
    "bazaar and souvenirs".toLowerCase() -> StoreSubtype.BAZAAR_AND_SOUVENIRS
    "drugstore and perfumery".toLowerCase() -> StoreSubtype.DRUGSTORE_PERFUMERY
    "optics".toLowerCase() -> StoreSubtype.OPTICS
    "photography".toLowerCase() -> StoreSubtype.PHOTO
    "tobacco and smoking articles".toLowerCase() -> StoreSubtype.TOBACCO_SMOKING_ARTICLES
    "prepared dishes".toLowerCase() -> StoreSubtype.PREPARED_DISHES
    "dry cleaner".toLowerCase() -> StoreSubtype.DRY_CLEANER
    "maintenance, cleaning, similar".toLowerCase() -> StoreSubtype.MAINTENANCE_CLEANING
    "repairs (appliances and cars)".toLowerCase() -> StoreSubtype.CAR_APPLIANCE_REPAIRING
    "veterinarian and pets".toLowerCase() -> StoreSubtype.VETERINARIANS_AND_PETS
    "shopping centre".toLowerCase() -> StoreSubtype.SHOPPING_CENTER
    "shopping gallery".toLowerCase() -> StoreSubtype.SHOPPING_GALLERY
    "supermarket".toLowerCase() -> StoreSubtype.SHOPPING_CENTER

    else -> StoreSubtype.OTHERS.also {
        Log.d("Parse json", "Subtype (${this@toStoreSubtype}) not classified!")
    }
}

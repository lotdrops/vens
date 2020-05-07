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
    "Bakery,Pastry and Dairy" -> StoreType.BAKERY_PASTRY_DAIRY
    "Drinks" -> StoreType.DRINKS
    "Eggs and birds" -> StoreType.EGGS_AND_BIRDS
    "Fashion" -> StoreType.FASHION
    "Fish and seafood" -> StoreType.FISH_AND_SEA_FOOD
    "Food" -> StoreType.FOOD
    "Fruit and vegetables" -> StoreType.FASHION
    "Health" -> StoreType.HEALTH
    "Home" -> StoreType.HOME
    "Leisure and culture" -> StoreType.LEISURE_AND_CULTURE
    "Look" -> StoreType.LOOK
    "Market" -> StoreType.MARKET
    "Meat" -> StoreType.MEAT
    "Others" -> StoreType.OTHER
    "Prepared dishes" -> StoreType.PREPARED_DISHES
    "Services" -> StoreType.SERVICES
    "Shopping centre" -> StoreType.SHOPPING_CENTER
    "Shopping gallery" -> StoreType.SHOPPING_GALLERY
    "Supermarket" -> StoreType.SUPERMARKET
    else -> StoreType.OTHER.also {
        Log.d("Parse json", "Type (${this@toStoreType}) not classified!")
    }
}

fun String.toStoreSubtype() = when (this.toLowerCase()) {
    "Bakery, Pastry and Dairy".toLowerCase() -> StoreSubtype.BAKERY_PASTRY_DAIRY
    "Drinks".toLowerCase() -> StoreSubtype.DRINKS
    "Eggs and birds".toLowerCase() -> StoreSubtype.EGGS_AND_BIRDS
    "Dress up".toLowerCase() -> StoreSubtype.DRESS
    "Footwear and leather".toLowerCase() -> StoreSubtype.FOOTWEAR_AND_LEATHER
    "Haberdashery".toLowerCase() -> StoreSubtype.HABERDASHERY
    "Jewelry and Watches".toLowerCase() -> StoreSubtype.JEWLERY_AND_WATCHES
    "Repairs".toLowerCase() -> StoreSubtype.REPAIRS
    "Fish and seafood".toLowerCase() -> StoreSubtype.FISH_AND_SEAFOOD
    "Others".toLowerCase() -> StoreSubtype.OTHERS
    "Fruits and vegetables".toLowerCase() -> StoreSubtype.FRUITS_VEGGIES
    "Health and Care".toLowerCase() -> StoreSubtype.HEALTH_AND_CARE
    "Herbalists, Dietetics and Nutrition".toLowerCase() -> StoreSubtype.DIETETICS_NUTRITION
    "Pharmacy and Parapharmacy".toLowerCase() -> StoreSubtype.PHARMACY_AND_PHARAFARMACY
    "Appliances".toLowerCase() -> StoreSubtype.APPLIANCES
    "Equipment".toLowerCase() -> StoreSubtype.EQUIPMENT
    "Flower shop".toLowerCase() -> StoreSubtype.FLOWER_SHOP
    "Furniture and Articles".toLowerCase() -> StoreSubtype.FURNITURE_AND_ARTICLES
    "Hardware store".toLowerCase() -> StoreSubtype.HARDWARE
    "Stamps, Coins and Antiques".toLowerCase() -> StoreSubtype.STAMPS_COINS_ANTIQUES
    "Bookshop, Newspapers and Magazines".toLowerCase() -> StoreSubtype.BOOKSHOP_NEWSPAPER_MAGAZINES
    "Computers".toLowerCase() -> StoreSubtype.COMPUTER_STORE
    "Music".toLowerCase() -> StoreSubtype.MUSIC
    "Toys and Sports".toLowerCase() -> StoreSubtype.TOYS_AND_SPORTS
    "Beauty Center".toLowerCase() -> StoreSubtype.BEAUTY_CENTER
    "Hair Salon".toLowerCase() -> StoreSubtype.HAIR_SALON
    "Market".toLowerCase() -> StoreSubtype.MARKET
    "Meat".toLowerCase() -> StoreSubtype.MEAT
    "Bazaar and Souvenirs".toLowerCase() -> StoreSubtype.BAZAAR_AND_SOUVENIRS
    "Drugstore and Perfumery".toLowerCase() -> StoreSubtype.DRUGSTORE_PERFUMERY
    "Optics".toLowerCase() -> StoreSubtype.OPTICS
    "Photography".toLowerCase() -> StoreSubtype.PHOTO
    "Tobacco and Smoking Articles".toLowerCase() -> StoreSubtype.TOBACCO_SMOKING_ARTICLES
    "Prepared dishes".toLowerCase() -> StoreSubtype.PREPARED_DISHES
    "Dry cleaner".toLowerCase() -> StoreSubtype.DRY_CLEANER
    "Maintenance, Cleaning, Similar".toLowerCase() -> StoreSubtype.MAINTENANCE_CLEANING
    "Repairs (Appliances and Cars)".toLowerCase() -> StoreSubtype.CAR_APPLIANCE_REPAIRING
    "Veterinarian and Pets".toLowerCase() -> StoreSubtype.VETERINARIANS_AND_PETS
    "Shopping Centre".toLowerCase() -> StoreSubtype.SHOPPING_CENTER
    "Shopping Gallery".toLowerCase() -> StoreSubtype.SHOPPING_GALLERY
    "Supermarket".toLowerCase() -> StoreSubtype.SHOPPING_CENTER

    else -> StoreSubtype.OTHERS.also {
        Log.d("Parse json", "Subtype (${this@toStoreSubtype}) not classified!")
    }
}

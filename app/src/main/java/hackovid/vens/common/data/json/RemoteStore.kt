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
    isFavourite = false,
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
    "alimentació" -> StoreType.GROCERY
    "bellesa" -> StoreType.BEAUTY
    "centre comercial" -> StoreType.MALL
    "galeria" -> StoreType.GALLERY
    "llar" -> StoreType.HOME
    "mercat" -> StoreType.MARKET
    "moda" -> StoreType.FASHION
    "oci i cultura" -> StoreType.LEISURE
    "quotidià" -> StoreType.EVERYDAY
    "serveis" -> StoreType.SERVICES
    "altres" -> StoreType.OTHER
    else -> StoreType.OTHER.also {
        Log.d("Parse json", "Type (${this@toStoreType}) not classified!")
    }
}

fun String.toStoreSubtype() = when (this.toLowerCase()) {
    "Altres".toLowerCase() -> StoreSubtype.OTHERS
    "Autoservei / supermercat".toLowerCase() -> StoreSubtype.SELF_SERVICE_SUPERMARKET
    "Begudes".toLowerCase() -> StoreSubtype.DRINKS
    "Carn i porc".toLowerCase() -> StoreSubtype.MEAT_PORK
    "Fruites i verdures".toLowerCase() -> StoreSubtype.FRUITS_VEGGIES
    "Ous i aus".toLowerCase() -> StoreSubtype.POULTRY
    "Pa, pastisseria i làctics".toLowerCase() -> StoreSubtype.BREAD_DAIRY
    "Peix i marisc".toLowerCase() -> StoreSubtype.FISH
    "Plats preparats (no degustació)".toLowerCase() -> StoreSubtype.READY_MEALS
    "Basars".toLowerCase() -> StoreSubtype.BAZAAR
    "Fotografia".toLowerCase() -> StoreSubtype.PHOTO
    "Souvenirs".toLowerCase() -> StoreSubtype.MEMORIES
    "Souvenirs i basars".toLowerCase() -> StoreSubtype.MEMORIES_BAZAARS
    "Òptiques ".toLowerCase() -> StoreSubtype.OPTICS
    "Òptiques i fotografia".toLowerCase() -> StoreSubtype.OPTICS_PHOTO
    "Centres d'estètica".toLowerCase() -> StoreSubtype.AESTHETICS
    "Perruqueries".toLowerCase() -> StoreSubtype.HAIRDRESSER
    "Centre comercial".toLowerCase() -> StoreSubtype.MALL
    "Galeria".toLowerCase() -> StoreSubtype.GALLERY
    "Aparells domèstics".toLowerCase() -> StoreSubtype.APPLIANCES
    "Floristeries".toLowerCase() -> StoreSubtype.FLOWER
    "Material equipament llar".toLowerCase() -> StoreSubtype.HOME_EQUIPMENT
    "Mobles i articles fusta i metall".toLowerCase() -> StoreSubtype.WOODEN_FURNITURE
    "Parament ferreteria".toLowerCase() -> StoreSubtype.HARDWARE
    "Segells, monedes i antiguitats".toLowerCase() -> StoreSubtype.STAMPS_SHOP
    "Mercat".toLowerCase() -> StoreSubtype.MARKET
    "Calçat i pell".toLowerCase() -> StoreSubtype.SHOE_SHOP
    "Joieria, rellotgeria i bijuteria".toLowerCase() -> StoreSubtype.JEWELRY_STORE
    "Merceria".toLowerCase() -> StoreSubtype.HABERDASHERY
    "Vestir".toLowerCase() -> StoreSubtype.DRESS
    "Informàtica".toLowerCase() -> StoreSubtype.COMPUTER_STORE
    "Joguines i esports".toLowerCase() -> StoreSubtype.TOYS_AND_SPORTS
    "Llibres, diaris i revistes".toLowerCase() -> StoreSubtype.BOOKS
    "Música".toLowerCase() -> StoreSubtype.MUSIC
    "Drogueria i perfumeria".toLowerCase() -> StoreSubtype.DRUGSTORE_PERFUMERY
    "Farmàcies i Parafarmàcies".toLowerCase() -> StoreSubtype.PHARMACY
    "Herbolaris, dietètica i Nutrició".toLowerCase() -> StoreSubtype.DIETETICS
    "Tabac i articles fumadors".toLowerCase() -> StoreSubtype.TOBACCO_SHOP
    "Manteniment, neteja i similars".toLowerCase() -> StoreSubtype.MAINTENANCE_CLEANING
    "Reparacions (Electrodomèstics i automòbils)".toLowerCase() ->
        StoreSubtype.CAR_APPLIANCE_REPAIRING
    "Sanitat i assistència".toLowerCase() -> StoreSubtype.HEALTH
    "Tintoreries".toLowerCase() -> StoreSubtype.DRY_CLEANER
    "Veterinaris / Mascotes".toLowerCase() -> StoreSubtype.VET_STORE
    else -> StoreSubtype.OTHERS.also {
        Log.d("Parse json", "Subtype (${this@toStoreSubtype}) not classified!")
    }
}

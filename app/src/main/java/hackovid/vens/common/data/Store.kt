package hackovid.vens.common.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hackovid.vens.R
import hackovid.vens.common.data.core.Converters

@Entity(tableName = "Stores")
data class Store(
    @PrimaryKey val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    @TypeConverters(Converters::class) val type: StoreType,
    @TypeConverters(Converters::class) val subtype: StoreSubtype,
    val phone: String? = null,
    val mobilePhone: String? = null,
    val address: String? = null,
    val web: String? = null,
    val email: String? = null,
    val schedule: String? = null,
    val acceptsOrders: Boolean? = null,
    val delivers: Boolean? = null
)

enum class StoreType(val textRes: Int) {
    BAKERY_PASTRY_DAIRY(R.string.store_type_bakery_pastry),
    DRINKS(R.string.store_subtype_drinks),
    EGGS_AND_BIRDS(R.string.store_type_eggs_and_birds),
    FASHION(R.string.store_type_fashion),
    FISH_AND_SEA_FOOD(R.string.store_subtype_fish),
    FOOD(R.string.store_type_food),
    FRUIT_AND_VEGETABLES(R.string.store_type_fruits_veggies),
    HEALTH(R.string.store_subtype_health),
    HOME(R.string.store_type_home),
    LEISURE_AND_CULTURE(R.string.store_type_leisure_and_culture),
    LOOK(R.string.store_type_look),
    MARKET(R.string.store_type_market),
    MEAT(R.string.store_subtype_meat_pork),
    OTHER(R.string.store_type_other),
    PREPARED_DISHES(R.string.store_type_prepared_dishes),
    SERVICES(R.string.store_type_services),
    SHOPPING_CENTER(R.string.store_type_mall),
    SHOPPING_GALLERY(R.string.store_type_gallery),
    SUPERMARKET(R.string.store_type_market)
}

enum class StoreSubtype(val textRes: Int) {
    BAKERY_PASTRY_DAIRY(R.string.store_subtype_bread_dairy),
    DRINKS(R.string.store_subtype_drinks),
    EGGS_AND_BIRDS(R.string.store_subtype_eggs_and_birds),
    DRESS(R.string.store_subtype_dress),
    FOOTWEAR_AND_LEATHER(R.string.store_subtype_footwear_leather),
    HABERDASHERY(R.string.store_subtype_haberdashery),
    JEWLERY_AND_WATCHES(R.string.store_subtype_jewelry_store),
    REPAIRS(R.string.store_subtype_repairs),
    FISH_AND_SEAFOOD(R.string.store_subtype_fish),
    OTHERS(R.string.store_subtype_others),
    FRUITS_VEGGIES(R.string.store_subtype_fruits_veggies),
    HEALTH_AND_CARE(R.string.store_subtype_health),
    DIETETICS_NUTRITION(R.string.store_subtype_dietetics),
    PHARMACY_AND_PHARAFARMACY(R.string.store_subtype_pharmacy),
    APPLIANCES(R.string.store_subtype_appliances),
    EQUIPMENT(R.string.store_subtype_equipment),
    FLOWER_SHOP(R.string.store_subtype_flower),
    FURNITURE_AND_ARTICLES(R.string.store_subtype_furniture),
    HARDWARE(R.string.store_subtype_hardware),
    STAMPS_COINS_ANTIQUES(R.string.store_subtype_stamps_coins_antiques),
    BOOKSHOP_NEWSPAPER_MAGAZINES(R.string.store_subtype_books),
    COMPUTER_STORE(R.string.store_subtype_computer_store),
    MUSIC(R.string.store_subtype_music),
    TOYS_AND_SPORTS(R.string.store_subtype_toys_and_sports),
    BEAUTY_CENTER(R.string.store_subtype_beauty_center),
    HAIR_SALON(R.string.store_subtype_hairdresser),
    MARKET(R.string.store_subtype_market),
    MEAT(R.string.store_subtype_meat_pork),
    BAZAAR_AND_SOUVENIRS(R.string.store_subtype_bazaar),
    DRUGSTORE_PERFUMERY(R.string.store_subtype_drugstore_perfumery),
    OPTICS(R.string.store_subtype_optics),
    PHOTO(R.string.store_subtype_photo),
    TOBACCO_SMOKING_ARTICLES(R.string.store_subtype_tobacco_shop),
    PREPARED_DISHES(R.string.store_subtype_prepared_dishes),
    DRY_CLEANER(R.string.store_subtype_dry_cleaner),
    MAINTENANCE_CLEANING(R.string.store_subtype_maintenance_cleaning),
    CAR_APPLIANCE_REPAIRING(R.string.store_subtype_car_appliance_repairing),
    VETERINARIANS_AND_PETS(R.string.store_subtype_vet_store),
    SHOPPING_CENTER(R.string.store_subtype_shopping_center),
    SHOPPING_GALLERY(R.string.store_subtype_shopping_gallery),
    SUPERMARKET(R.string.store_subtype_market),
}

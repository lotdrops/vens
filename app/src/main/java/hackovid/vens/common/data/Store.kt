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
    val isFavourite: Boolean = false,
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
    GROCERY(R.string.store_type_grocery),
    BEAUTY(R.string.store_type_beauty),
    MALL(R.string.store_type_mall),
    GALLERY(R.string.store_type_gallery),
    HOME(R.string.store_type_home),
    MARKET(R.string.store_type_market),
    FASHION(R.string.store_type_fashion),
    LEISURE(R.string.store_type_leisure),
    EVERYDAY(R.string.store_type_everyday),
    SERVICES(R.string.store_type_services),
    OTHER(R.string.store_type_other)
}

enum class StoreSubtype(val textRes: Int) {
    OTHERS(R.string.store_subtype_others),
    SELF_SERVICE_SUPERMARKET(R.string.store_subtype_self_service_supermarket),
    DRINKS(R.string.store_subtype_drinks),
    MEAT_PORK(R.string.store_subtype_meat_pork),
    FRUITS_VEGGIES(R.string.store_subtype_fruits_veggies),
    POULTRY(R.string.store_subtype_poultry),
    BREAD_DAIRY(R.string.store_subtype_bread_dairy),
    FISH(R.string.store_subtype_fish),
    READY_MEALS(R.string.store_subtype_ready_meals),
    BAZAAR(R.string.store_subtype_bazaar),
    PHOTO(R.string.store_subtype_photo),
    MEMORIES(R.string.store_subtype_memories),
    MEMORIES_BAZAARS(R.string.store_subtype_memories_bazaars),
    OPTICS(R.string.store_subtype_optics),
    OPTICS_PHOTO(R.string.store_subtype_optics_photo),
    AESTHETICS(R.string.store_subtype_aesthetics),
    HAIRDRESSER(R.string.store_subtype_hairdresser),
    MALL(R.string.store_subtype_mall),
    GALLERY(R.string.store_subtype_gallery),
    APPLIANCES(R.string.store_subtype_appliances),
    FLOWER(R.string.store_subtype_flower),
    HOME_EQUIPMENT(R.string.store_subtype_home_equipment),
    WOODEN_FURNITURE(R.string.store_subtype_wooden_furniture),
    HARDWARE(R.string.store_subtype_hardware),
    STAMPS_SHOP(R.string.store_subtype_stamps_shop),
    MARKET(R.string.store_subtype_market),
    SHOE_SHOP(R.string.store_subtype_shoe_shop),
    JEWELRY_STORE(R.string.store_subtype_jewelry_store),
    HABERDASHERY(R.string.store_subtype_haberdashery),
    DRESS(R.string.store_subtype_dress),
    COMPUTER_STORE(R.string.store_subtype_computer_store),
    TOYS_AND_SPORTS(R.string.store_subtype_toys_and_sports),
    BOOKS(R.string.store_subtype_books),
    MUSIC(R.string.store_subtype_music),
    DRUGSTORE_PERFUMERY(R.string.store_subtype_drugstore_perfumery),
    PHARMACY(R.string.store_subtype_pharmacy),
    DIETETICS(R.string.store_subtype_dietetics),
    TOBACCO_SHOP(R.string.store_subtype_tobacco_shop),
    MAINTENANCE_CLEANING(R.string.store_subtype_maintenance_cleaning),
    CAR_APPLIANCE_REPAIRING(R.string.store_subtype_car_appliance_repairing),
    HEALTH(R.string.store_subtype_health),
    DRY_CLEANER(R.string.store_subtype_dry_cleaner),
    VET_STORE(R.string.store_subtype_vet_store),
}

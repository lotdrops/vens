package hackovid.vens.features.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.gms.maps.model.LatLng
import hackovid.vens.R
import hackovid.vens.common.data.login.FirebaseResponse
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.common.utils.SingleLiveEvent
import hackovid.vens.common.utils.combineLiveDatas
import hackovid.vens.common.utils.combineWith
import hackovid.vens.features.register.RegisterFieldsValidator.Companion.MIN_ADDRESS_LENGTH
import hackovid.vens.features.register.RegisterFieldsValidator.Companion.MIN_NAME_LENGTH
import kotlin.math.absoluteValue

class FillStoreInfoViewModel(
    private val storeId: Long?,
    private val dataSource: RemoteDataSource<FirebaseResponse>,
    private val validator: RegisterFieldsValidator
) : ViewModel() {
    private val isEditing = storeId != null

    val selectStoreEvent = SingleLiveEvent<Unit>()
    val selectLocationEvent = SingleLiveEvent<Unit>()
    val registerEvent = SingleLiveEvent<Unit>()

    val title =
        if (isEditing) R.string.store_info_edit_title
        else R.string.store_info_create_title
    val buttonText =
        if (isEditing) R.string.store_info_edit_button
        else R.string.store_info_create_button

    val name = MutableLiveData("")
    val location = MutableLiveData<LatLng?>(null)
    val type = MutableLiveData(-1)
    val subtype = MutableLiveData(-1)
    val address = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val mobilePhone = MutableLiveData<String>()
    val web = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val schedule = MutableLiveData<String>()
    val acceptsOrders = MutableLiveData(false)
    val delivers = MutableLiveData(false)

    val nameError = MutableLiveData<Int?>(null)
    val addressError = MutableLiveData<Int?>(null)
    val typeError = MutableLiveData<Int?>(null)
    val subtypeError = MutableLiveData<Int?>(null)
    val locationError = MutableLiveData<Int?>(null)

    val showLocationNotSet = location.combineWith(locationError) { location, error ->
        location == null && (error == null || error == 0)
    }
    val showLocationError = location.combineWith(locationError) { location, error ->
        location == null && error != null && error != 0
    }

    val latitude = location.map { it?.latitude?.formatDecimals() ?: "" }
    val longitude = location.map { it?.longitude?.formatDecimals() ?: "" }

    init {
        name.observeForever {
            Log.d("asddd", "name:$it")
        }
        address.observeForever {
            Log.d("asddd", "address:$it")
        }
    }

    fun onLocationClicked() {
        selectLocationEvent.call()
    }

    fun onButtonClick() {
        validateFields()
        if (!anyErrorRemaining()) {
            // TODO
        }
    }

    private fun validateFields() {
        val name = name.value
        val address = address.value
        val type = type.value
        val subType = subtype.value
        nameError.value = if (!name.isNullOrEmpty() && !validator.isValidName(name)) {
            R.string.register_empty_field_error
        } else null
        addressError.value = if (!address.isNullOrEmpty() && !validator.isValidAddress(address)) {
            R.string.register_empty_field_error
        } else null
        typeError.value = if ((type ?: -1) < 0) R.string.register_empty_field_error else null
        subtypeError.value = if ((subType ?: -1) < 0) R.string.register_empty_field_error else null
        locationError.value =
            if (location.value == null) R.string.register_empty_field_error
            else null
    }
    private fun anyErrorRemaining() = typeError.value != null || subtypeError.value != null ||
            locationError.value != null || nameError.value != null || addressError.value != null

    private fun Double.formatDecimals() = "%.${LOCATION_DECIMALS}f".format(this.absoluteValue)
}
private const val LOCATION_DECIMALS = 7

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
    val location = MutableLiveData<LatLng?>()
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

    val emailError = MutableLiveData<Int?>(0)
    val latitude = location.map { it?.latitude?.formatDecimals() ?: "" }
    val longitude = location.map { it?.longitude?.formatDecimals() ?: "" }

    val buttonEnabled: LiveData<Boolean> = combineLiveDatas(name, location, address) {
        name.value?.trim()?.length ?: 0 >= MIN_NAME_LENGTH && location.value != null &&
                address.value?.trim()?.length ?: 0 >= MIN_ADDRESS_LENGTH
    }
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
        val email = email.value
        emailError.value = if (!email.isNullOrEmpty() && !validator.isValidEmail(email)) {
            R.string.register_mail_is_incorrect
        } else null
    }
    private fun anyErrorRemaining() = emailError.value != null

    private fun Double.formatDecimals() = "%.${LOCATION_DECIMALS}f".format(this.absoluteValue)
}
private const val LOCATION_DECIMALS = 7

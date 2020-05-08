package hackovid.vens.common.ui

import android.content.Context
import androidx.databinding.InverseMethod
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType

object BindingConverter {
    fun storeTypePositionToString(context: Context, index: Int): String =
        if (index < 0) ""
        else context.resources.getString(StoreType.values()[index].textRes)

    @InverseMethod(value = "storeTypePositionToString")
    fun stringToStoreTypePosition(context: Context, string: String): Int =
        StoreType.values().indexOfFirst { context.resources.getString(it.textRes) == string }

    fun storeSubtypePositionToString(context: Context, index: Int): String =
        if (index < 0) ""
        else context.resources.getString(StoreSubtype.values()[index].textRes)

    @InverseMethod(value = "storeSubtypePositionToString")
    fun stringToStoreSubtypePosition(context: Context, string: String): Int =
        StoreSubtype.values().indexOfFirst { context.resources.getString(it.textRes) == string }
}

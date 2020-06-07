package hackovid.vens.common.data.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(
    val userName: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = ""
) : Parcelable

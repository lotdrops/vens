package hackovid.vens.common.data.login

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userName: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = ""
)

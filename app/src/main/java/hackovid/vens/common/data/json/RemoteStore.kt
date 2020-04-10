package hackovid.vens.common.data.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteStore (
    val id: Int = 0,
    @Json(name = "lat") val latitude: Double,
    @Json(name = "long") val longitude: Double,
     val name: String,
     val type: String,
     val adress: String
)

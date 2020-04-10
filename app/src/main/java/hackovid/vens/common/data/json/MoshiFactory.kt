package hackovid.vens.common.data.json

import com.squareup.moshi.Moshi

object MoshiFactory {

    private val moshi : Moshi =  Moshi.Builder().build()
    fun getInstance() = moshi
}

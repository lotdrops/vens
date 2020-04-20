package hackovid.vens.common.data.core

import android.location.Location

fun Location.distance(latitude: Double, longitude: Double) = Location("").apply {
    setLatitude(latitude)
    setLongitude(longitude)
}.distanceTo(this)

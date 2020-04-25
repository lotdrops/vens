package hackovid.vens.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager

fun Context.hasLocationPermission() =
    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

fun Activity.requestLocationPermission(reqCode: Int) {
    val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)
    requestPermissions(permissions, reqCode)
}

@SuppressLint("MissingPermission")
fun Activity.getImmediateLocation(): Location? {
    val locationManager: LocationManager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val providers: List<String> = locationManager.getProviders(true)
    var userLocation: Location? = null
    for (i in providers.size - 1 downTo 0) {
        userLocation = locationManager.getLastKnownLocation(providers[i])
        if (userLocation != null) {
            break
        }
    }
    return userLocation
}

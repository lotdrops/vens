package hackovid.vens.common.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import hackovid.vens.R
import hackovid.vens.common.utils.getImmediateLocation
import hackovid.vens.common.utils.hasLocationPermission
import hackovid.vens.common.utils.observe
import hackovid.vens.common.utils.requestLocationPermission
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: SharedViewModel by viewModel()

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { newLoc ->
                viewModel.onNewLocation(LatLng(newLoc.latitude, newLoc.longitude))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host)
        navView.setupWithNavController(navController)
        fetchLocation()
        observe(viewModel.requestLocationEvent) { onLocationRequested() }
    }

    private fun onLocationRequested() {
        stopLocationUpdates()
        requestLocationPermission(USER_REQUESTED_LOCATION)
    }

    override fun onStart() {
        super.onStart()
        if (hasLocationPermission()) startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    override fun onBackPressed() {
        findNavController(R.id.nav_host).apply {
            val backHandled = getCurrentFragment()?.onBackPressed() ?: false
            if (!backHandled) super.onBackPressed()
        }
    }

    private fun getCurrentFragment(): BaseFragment<*>? {
        return try {
            nav_host?.childFragmentManager?.fragments?.firstOrNull() as? BaseFragment<*>
        } catch (ex: Throwable) {
            null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQ_CODE_LOCATION -> {
                if ((grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED)) {
                    startLocationUpdates()
                }
            }
            USER_REQUESTED_LOCATION -> {
                if ((grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED)) {
                    viewModel.onLocationAccepted.call()
                    startLocationUpdates()
                }
            }
        }
    }

    private fun fetchLocation() {
        if (hasLocationPermission()) {
            val location = getImmediateLocation()
            if (location != null) {
                viewModel.onNewLocation(LatLng(location.latitude, location.longitude))
            }
        } else {
            requestLocationPermission(REQ_CODE_LOCATION)
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest().apply {
            priority = PRIORITY_BALANCED_POWER_ACCURACY
            interval = TimeUnit.SECONDS.toMillis(15)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
        }
        fusedLocationClient?.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }
}

private const val REQ_CODE_LOCATION = 100
private const val USER_REQUESTED_LOCATION = 101

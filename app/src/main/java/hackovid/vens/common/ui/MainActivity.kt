package hackovid.vens.common.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import hackovid.vens.BuildConfig
import hackovid.vens.R
import hackovid.vens.common.utils.getImmediateLocation
import hackovid.vens.common.utils.hasLocationPermission
import hackovid.vens.common.utils.observe
import hackovid.vens.common.utils.requestLocationPermission
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


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
        observe(viewModel.userRequestsLocationEvent) { onLocationRequestedByUser() }
        observe(viewModel.requestLocationEvent) {
            if (!hasLocationPermission()) requestLocationPermission(FIRST_TIME_LOCATION)
        }
        observe(viewModel.onObserverActive) {}
    }

    private fun onLocationRequestedByUser() {
        stopLocationUpdates()
        requestLocationPermission(USER_REQUESTED_LOCATION)
    }

    override fun onStart() {
        super.onStart()
        if (hasLocationPermission()) startLocationUpdates()
        checkMinAppVersion()
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
            FIRST_TIME_LOCATION -> {
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

    private fun checkMinAppVersion() {
       if(viewModel.minForcedVersion > BuildConfig.VERSION_CODE) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.app_new_version_available)
            .setMessage(R.string.app_new_version_update_please)
            .setPositiveButton(R.string.app_new_version_update) { _ , _ -> redirectStore(viewModel.updateStoreUrl) }
            dialog.show()
       }
    }

    private fun redirectStore(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}

private const val FIRST_TIME_LOCATION = 100
private const val USER_REQUESTED_LOCATION = 101

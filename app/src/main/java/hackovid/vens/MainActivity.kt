package hackovid.vens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import hackovid.vens.common.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var userLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host)
        navView.setupWithNavController(navController)
        fetchLocation()
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
        when(requestCode) {
            REQ_CODE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    fetchLocation()
                }
            }
        }
    }

    private fun fetchLocation() {

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                requestPermissions(permissions, REQ_CODE_LOCATION)
                return
        }

        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)

        for (i in providers.size - 1 downTo 0) {
            //TODO: store on SharedViewModel
            userLocation = locationManager.getLastKnownLocation(providers[i])
            if (userLocation != null) break
        }
    }

    companion object {
        const val REQ_CODE_LOCATION = 100
    }
}

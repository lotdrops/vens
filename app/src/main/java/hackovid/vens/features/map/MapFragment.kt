package hackovid.vens.features.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.view.doOnLayout
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.clustering.ClusterManager
import com.google.android.gms.maps.model.LatLng
import hackovid.vens.R
import hackovid.vens.common.data.toClusterStoreItem
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentMapBinding
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {
    override val layoutRes = R.layout.fragment_map

    private val viewModel: MapViewModel by viewModel()

    private val mapBottomPadding = MutableLiveData(0)
    private lateinit var googleMap: GoogleMap

    override fun setupBinding(binding: FragmentMapBinding) {
        binding.viewModel = viewModel
        binding.setupViews()
        subscribeUi()
        fetchLocation()
        setupMap()
    }

    override fun onBackPressed(): Boolean = viewModel.onBackPressed()

    private fun FragmentMapBinding.setupViews() {
        root.infoLayout.doOnLayout {
            mapBottomPadding.value = it.height +
                    it.resources.getDimension(R.dimen.map_info_margin).roundToInt()
        }
    }

    private fun subscribeUi() {
        observe(mapBottomPadding) { padding ->
            if (padding != null && this::googleMap.isInitialized) {
                googleMap.setBottomPadding(padding)
            }
        }

        observe(viewModel.location) {
            if (this::googleMap.isInitialized) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            }
        }
    }

    private fun setupMap() {
        val mapFragment = SupportMapFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.mapContainerView, mapFragment)
            ?.commit()
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.setOnMapLongClickListener { latLng -> viewModel.location.value = latLng }
        viewModel.location.value?.let { latLng ->
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        mapBottomPadding.value?.let { padding -> googleMap.setBottomPadding(padding) }
        observeStores()
        if (context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            context?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED)

            googleMap.isMyLocationEnabled = true
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

        if(context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
            || context?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(permissions, REQ_CODE_LOCATION)
            return
        }

        val locationManager: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null

        for (i in providers.size - 1 downTo 0) {
            location = locationManager.getLastKnownLocation(providers[i])
            if (location != null) break
        }

        location?.let { loc ->
            LatLng(loc.latitude, loc.longitude)
            viewModel.location.value = LatLng(loc.latitude, loc.longitude)
        }

        if (this::googleMap.isInitialized) googleMap.isMyLocationEnabled = true
    }

    private fun observeStores() {
        observe(viewModel.stores) { stores ->
            val clusterStores: ClusterManager<ClusterStoreItem>  = ClusterManager<ClusterStoreItem>(activity, googleMap);
            stores.forEach { store ->
                clusterStores.addItem(store.toClusterStoreItem())
            }
            clusterStores.cluster()
        }
    }

    private fun GoogleMap.setBottomPadding(padding: Int) = setPadding(0, 0, 0, padding)

    companion object {
        const val REQ_CODE_LOCATION = 100
    }
}

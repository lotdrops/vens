package hackovid.vens.features.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnLayout
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import hackovid.vens.R
import hackovid.vens.common.ui.DEFAULT_LAT
import hackovid.vens.common.ui.DEFAULT_LONG
import hackovid.vens.common.ui.FilterBaseFragment
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentMapBinding
import kotlin.math.roundToInt
import kotlinx.android.synthetic.main.fragment_map.mapFilterButton
import kotlinx.android.synthetic.main.fragment_map.view.infoLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MapFragment : FilterBaseFragment<FragmentMapBinding>(), GoogleMap.OnMapClickListener,
    OnMapReadyCallback, ClusterManager.OnClusterItemClickListener<ClusterStoreItem> {

    override val layoutRes = R.layout.fragment_map

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val viewModel: MapViewModel by viewModel { parametersOf(sharedViewModel) }

    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<ClusterStoreItem>
    private lateinit var renderer: ClusterStoreRenderer

    override fun setupBinding(binding: FragmentMapBinding) {
        binding.viewModel = viewModel
        binding.setupViews()
        subscribeUi()
        setupMap()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFilterButton.setOnClickListener { onFilterClicked() }
    }

    override fun onBackPressed(): Boolean = viewModel.onBackPressed()

    private fun FragmentMapBinding.setupViews() {
        root.infoLayout.doOnLayout {
            this@MapFragment.viewModel.setCardMapPadding(it.height +
                    it.resources.getDimension(R.dimen.map_info_margin).roundToInt())
        }
    }

    private fun subscribeUi() {
        observe(viewModel.mapBottomPadding) { padding ->
            if (padding != null && this::googleMap.isInitialized) {
                googleMap.setBottomPadding(padding)
            }
        }

        observe(sharedViewModel.location) {
            if (this::googleMap.isInitialized) {
                locateUserOnMap()
            }
        }
        observe(viewModel.navigateToDetail) { storeId ->
            NavHostFragment.findNavController(this)
                .navigate(MapFragmentDirections.navToAdDetail(storeId))
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
        googleMap.styleMap()
        googleMap.setOnMapLongClickListener { latLng ->
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
        val location = sharedViewModel.location.value ?: LatLng(DEFAULT_LAT, DEFAULT_LONG)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.uiSettings.isMapToolbarEnabled = false
        setUpClusterer()
        observeStores()
        locateUserOnMap()
    }

    private fun GoogleMap.styleMap() {
        try {
            setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style))
        } catch (e: Exception) {
            Log.e("MapFragment", "Error loading map style: $e")
        }
    }

    private fun locateUserOnMap() {
        if (context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            context?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        )
            if (this::googleMap.isInitialized) {
                googleMap.isMyLocationEnabled = true
                googleMap.setOnMapClickListener(this)
                googleMap.uiSettings.isMyLocationButtonEnabled = false
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLng(sharedViewModel.location.value)
                )
            }
    }

    private fun observeStores() {
        observe(viewModel.stores) { stores ->
            clusterManager.clearItems()
            clusterManager.addItems(stores)
            clusterManager.setOnClusterItemClickListener(this)
            clusterManager.setAlgorithm(NonHierarchicalViewBasedAlgorithm(
                resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
            )
            clusterManager.cluster()
        }
    }

    private fun setUpClusterer() {
        clusterManager = ClusterManager(context, googleMap)
        googleMap.setOnMarkerClickListener(clusterManager)
        clusterManager.renderer = context?.let {
            ClusterStoreRenderer(it, googleMap, clusterManager).apply { renderer = this }
        }
        googleMap.setOnCameraIdleListener(clusterManager)
    }

    private fun GoogleMap.setBottomPadding(padding: Int) = setPadding(0, 0, 0, padding)

    override fun onClusterItemClick(item: ClusterStoreItem?): Boolean {
        viewModel.selectedStoreId.value = item?.id?.toInt()
        if (this::renderer.isInitialized) renderer.onClusterItemSelected(item)
        return true
    }

    override fun onMapClick(p0: LatLng?) {
        viewModel.selectedStoreId.value = null
        if (this::renderer.isInitialized) renderer.onClusterItemSelected(null)
    }
}

package hackovid.vens.features.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import hackovid.vens.R
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

    private val mapBottomPadding = MutableLiveData(0)
    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<ClusterStoreItem>

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
        googleMap.setOnMapLongClickListener { latLng -> sharedViewModel.location.value = latLng }
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sharedViewModel.location.value))
        googleMap.uiSettings.isMapToolbarEnabled = false
        mapBottomPadding.value?.let { padding -> googleMap.setBottomPadding(padding) }
        setUpClusterer()
        observeStores()
        locateUserOnMap()
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
            ClusterStoreRenderer(it, googleMap, clusterManager)
        }
        googleMap.setOnCameraIdleListener(clusterManager)
    }

    private fun GoogleMap.setBottomPadding(padding: Int) = setPadding(0, 0, 0, padding)

    override fun onClusterItemClick(item: ClusterStoreItem?): Boolean {
        viewModel.selectedStoreId.value = item?.id?.toInt()
        return true
    }

    override fun onMapClick(p0: LatLng?) {
        viewModel.selectedStoreId.value = null
    }
}

package hackovid.vens.features.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.view.doOnLayout
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import hackovid.vens.MainActivity
import hackovid.vens.R
import hackovid.vens.common.data.toClusterStoreItem
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentMapBinding
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt


class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback, ClusterManager.OnClusterItemClickListener<ClusterStoreItem>{

    override val layoutRes = R.layout.fragment_map

    private val viewModel: MapViewModel by viewModel()

    private val mapBottomPadding = MutableLiveData(0)
    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<ClusterStoreItem>

    override fun setupBinding(binding: FragmentMapBinding) {
        binding.viewModel = viewModel
        binding.setupViews()
        subscribeUi()
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
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isZoomControlsEnabled = false
        viewModel.location.value?.let { latLng ->
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        mapBottomPadding.value?.let { padding -> googleMap.setBottomPadding(padding) }
        setUpClusterer()
        observeStores()
        locateUserOnMap(googleMap)
    }

    private fun locateUserOnMap(googleMap: GoogleMap) {
        if (context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            context?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        )

            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            (activity as MainActivity).userLocation?.let { loc ->
                LatLng(loc.latitude, loc.longitude)
                viewModel.location.value = LatLng(loc.latitude, loc.longitude)
            }
    }

    private fun observeStores() {
        observe(viewModel.stores) { stores ->
            stores.forEach { store ->
                clusterManager.addItem(store.toClusterStoreItem())
            }
            clusterManager.setOnClusterItemClickListener(this)
            clusterManager.setAlgorithm(NonHierarchicalViewBasedAlgorithm(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels))
            clusterManager.cluster()
        }
    }

    private fun setUpClusterer() {
        clusterManager = ClusterManager(context, googleMap)
        googleMap.setOnCameraIdleListener(clusterManager)
    }

    private fun GoogleMap.setBottomPadding(padding: Int) = setPadding(0, 0, 0, padding)

    override fun onClusterItemClick(item: ClusterStoreItem?): Boolean {
        viewModel.selectedStoreId.value = item?.toStoreItem()?.id?.toInt()
        return true
    }
}

package hackovid.vens.features.map

import androidx.core.view.doOnLayout
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.clustering.ClusterManager
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
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(12f))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        mapBottomPadding.value?.let { padding -> googleMap.setBottomPadding(padding) }
        observeStores()
    }

    private fun GoogleMap.setBottomPadding(padding: Int) = setPadding(0, 0, 0, padding)

    private fun observeStores() {
        observe(viewModel.stores) { stores ->
            val clusterStores: ClusterManager<ClusterStoreItem>  = ClusterManager<ClusterStoreItem>(activity, googleMap);
            stores.forEach { store ->
                clusterStores.addItem(store.toClusterStoreItem())
            }
            clusterStores.cluster()
        }
    }
}

package hackovid.vens.features.register

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.DEFAULT_LAT
import hackovid.vens.common.ui.DEFAULT_LONG
import hackovid.vens.databinding.FragmentLocateStoreOnMapBinding
import hackovid.vens.features.onboarding.di.OnBoardingSharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LocateStoreOnMapFragment : BaseFragment<FragmentLocateStoreOnMapBinding>(),
    OnMapReadyCallback, GoogleMap.OnMapClickListener {
    override val layoutRes = R.layout.fragment_locate_store_on_map

    private val viewModel: LocateStoreOnMapViewModel by viewModel()
    private val onBoardingSharedViewModel: OnBoardingSharedViewModel by viewModel()
    private val args: LocateStoreOnMapFragmentArgs by navArgs()
    private lateinit var binding: FragmentLocateStoreOnMapBinding

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker

    override fun setupBinding(binding: FragmentLocateStoreOnMapBinding) {
        this.binding = binding
        binding.viewModel = viewModel
        binding.fragment = this
        setupMap()
    }

    fun onButtonClick() {
        onBoardingSharedViewModel.onMapResult = marker.position
        findNavController().popBackStack()
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
        //googleMap.styleMap()
        googleMap.setOnMapLongClickListener { latLng ->
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
        locateSelectedStore()
        googleMap.setOnMapClickListener(this)
    }

    private fun locateSelectedStore() {
        if (args.location != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(args.location))
            val latLng = args.location?.let { LatLng(it.latitude, it.longitude) }
            latLng?.let { addMarker(it) }
        } else {
            val location = LatLng(DEFAULT_LAT, DEFAULT_LONG)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        }
    }

    private fun addMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng)
        marker = googleMap.addMarker(markerOptions)
    }

    override fun onMapClick(latLng: LatLng) {
        viewModel.enableButton.value = true

        if (!this::marker.isInitialized) {
            addMarker(latLng)
            return
        }
        marker.position = latLng
    }
}

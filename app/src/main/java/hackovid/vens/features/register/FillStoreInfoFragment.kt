package hackovid.vens.features.register

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hackovid.vens.R
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.DEFAULT_LAT
import hackovid.vens.common.ui.DEFAULT_LONG
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentFillStoreInfoBinding
import hackovid.vens.features.onboarding.di.OnBoardingSharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FillStoreInfoFragment : BaseFragment<FragmentFillStoreInfoBinding>(), OnMapReadyCallback {
    override val layoutRes = R.layout.fragment_fill_store_info

    private val args: FillStoreInfoFragmentArgs by navArgs()
    private val viewModel: FillStoreInfoViewModel by viewModel { parametersOf(args.storeId) }
    private val onBoardingSharedViewModel: OnBoardingSharedViewModel by viewModel()

    private var marker: Marker? = null

    override fun onStart() {
        super.onStart()
        val mapResult = onBoardingSharedViewModel.onMapResult
        if (mapResult != null) {
            viewModel.location.value = mapResult
            onBoardingSharedViewModel.onMapResult = null
        }
    }

    override fun onStop() {
        super.onStop()
        marker = null
    }

    override fun setupBinding(binding: FragmentFillStoreInfoBinding) {
        binding.viewModel = viewModel
        binding.setupViews()
        subscribeToVm(binding)
        setupMap()
    }

    private fun FragmentFillStoreInfoBinding.setupViews() {
        context?.let { context ->
            val subTypes = StoreSubtype.values()
                .map { context.resources.getString(it.textRes) }
                .toTypedArray()
            subtype.setDropdownEntries(subTypes)
            val types = StoreType.values()
                .map { context.resources.getString(it.textRes) }
                .toTypedArray()
            type.setDropdownEntries(types)
        }
    }

    private fun subscribeToVm(binding: FragmentFillStoreInfoBinding) {
        observe(viewModel.selectStoreEvent) {
            NavHostFragment.findNavController(this).navigate(
                RegisterFragmentDirections.navToSelectStoreFragment()
            )
        }
        observe(viewModel.selectLocationEvent) {
            NavHostFragment.findNavController(this).navigate(
                FillStoreInfoFragmentDirections.navToLocateStoreOnMap(viewModel.location.value)
            )
        }
        observe(viewModel.registerEvent) {
            // TODO create screen and navigate
        }
        observe(viewModel.scrollToTopEvent) {
            binding.scrollview.smoothScrollTo(0, 0)
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
        googleMap.styleMap()
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        val storeLocation = viewModel.location.value
        val location = storeLocation ?: LatLng(DEFAULT_LAT, DEFAULT_LONG)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        if (storeLocation != null) googleMap.setMarker(storeLocation)
        observe(viewModel.location) { latLng ->
            if (latLng != null) googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap.setMarker(latLng)
        }
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.isMyLocationEnabled = false
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.setOnMapClickListener { viewModel.onLocationClicked() }
    }

    private fun GoogleMap.styleMap() {
        try {
            setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style))
        } catch (e: Exception) {
            Log.e("MapFragment", "Error loading map style: $e")
        }
    }

    private fun GoogleMap.setMarker(latLng: LatLng?) {
        if (latLng == null) {
            marker?.remove()
            marker = null
        } else {
            if (marker == null) {
                val markerOptions = MarkerOptions().position(latLng)
                marker = addMarker(markerOptions)
            } else {
                marker!!.position = latLng
            }
        }
    }
}

private fun AutoCompleteTextView.setDropdownEntries(elements: Array<String>) {
    setAdapter((ArrayAdapter(context, R.layout.item_dropdown_standard, elements)))
    threshold = 9999 // bug where only 1 option is displayed with preselection
    inputType = 0
}

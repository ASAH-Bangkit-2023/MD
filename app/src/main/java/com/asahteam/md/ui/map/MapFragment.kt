package com.asahteam.md.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asahteam.md.R
import com.asahteam.md.databinding.FragmentMapBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.remote.response.ResultsItem
import com.asahteam.md.ui.utils.MapsViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {
    private val viewModel by viewModels<MapsViewModel> {
        MapsViewModelFactory.getInstance()
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding
    private val locations = mutableListOf<ResultsItem>()
    private val marker = mutableListOf<Marker>()

    private val callback = OnMapReadyCallback { googleMap ->
        val boundsBuilder = LatLngBounds.Builder()
        marker.forEach {
            it.remove()
        }
        marker.clear()

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            binding?.let {
                it.checkLocation.visibility = View.VISIBLE
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        location?.let {
            val mark = googleMap.addMarker(
                MarkerOptions().position(
                    LatLng(
                        it.latitude,
                        it.longitude
                    )
                ).title("Your Location")
            )
            boundsBuilder.include(LatLng(it.latitude, it.longitude))
        }
        locations.forEach {
            val lat = it.geometry.location.lat as Double
            val lng = it.geometry.location.lng as Double
            val loc = LatLng(lat, lng)
            val mark = googleMap.addMarker(
                MarkerOptions().position(loc).title(it.name)
            )
            mark?.let { it1 -> marker.add(it1) }
            boundsBuilder.include(loc)
        }
        if (locations.isNotEmpty() || location != null) {
            val bounds: LatLngBounds = boundsBuilder.build()
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                getMyLastLocation()
                makeMaps()
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    this@MapFragment.location = location
                } else {
                    Toast.makeText(
                        context,
                        "Lokasi Tidak Ditemukan, Silakan Coba Lagi",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun makeMaps() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        makeMaps()
        binding?.let { loc ->
            loc.checkLocation.setOnClickListener {
                getMyLastLocation()
                location?.let {
                    viewModel.setLocation(it.latitude, it.longitude)
                }
            }
        }
        viewModel.maps.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultResponse.Error -> {
                    binding?.let {
                        it.progressBar.visibility = View.GONE
                        it.notFoundTv.visibility = View.GONE
                    }
                    Toast.makeText(context, result.error, Toast.LENGTH_LONG).show()
                    Log.e("mapsError", result.error)
                }

                is ResultResponse.Loading -> {
                    binding?.let {
                        it.progressBar.visibility = View.VISIBLE
                        it.notFoundTv.visibility = View.GONE
                    }
                }

                is ResultResponse.NotFound -> {
                    binding?.let {
                        it.progressBar.visibility = View.GONE
                        it.notFoundTv.visibility = View.VISIBLE
                    }
                }

                is ResultResponse.Success -> {
                    binding?.let {
                        it.progressBar.visibility = View.GONE
                        it.notFoundTv.visibility = View.GONE
                    }
                    locations.clear()
                    locations.addAll(result.data.results)
                    makeMaps()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
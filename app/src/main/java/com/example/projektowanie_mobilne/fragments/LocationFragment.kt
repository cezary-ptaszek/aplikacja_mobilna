package com.example.projektowanie_mobilne.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.projektowanie_mobilne.R
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import android.location.Location
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.Marker
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationFragment : Fragment() {

    private lateinit var map: MapView
    private val REQUEST_LOCATION_PERMISSION = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        // Inicjalizacja mapy
        map = view.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        // Dodaj przycisk do odświeżania lokalizacji
        val locationButton: Button = view.findViewById(R.id.btn_get_location)
        locationButton.setOnClickListener {
            getLocationAndUpdateMap()
        }

        return view
    }

    private fun getLocationAndUpdateMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }

        // Pobierz lokalizację (asynchronicznie)
        lifecycleScope.launch {
            val location = getUserLocation()
            if (location != null) {
                updateMap(location.latitude, location.longitude)
            } else {
                Toast.makeText(requireContext(), "Nie udało się pobrać lokalizacji", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun getUserLocation(): Location? {
        return withContext(Dispatchers.IO) {
            val locationManager = requireContext().getSystemService(android.content.Context.LOCATION_SERVICE)
                    as android.location.LocationManager
            val provider = locationManager.getBestProvider(android.location.Criteria(), true)

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Jeśli brak wymaganych uprawnień
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Brak uprawnień do lokalizacji", Toast.LENGTH_SHORT).show()
                }
                return@withContext null
            }

            provider?.let {
                locationManager.getLastKnownLocation(it)
            }
        }
    }

    private fun updateMap(latitude: Double, longitude: Double) {
        val startPoint = GeoPoint(latitude, longitude)
        map.controller.setZoom(15.0)
        map.controller.setCenter(startPoint)

        // Dodaj marker
        val marker = Marker(map)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Twoja lokalizacja"
        map.overlays.clear()
        map.overlays.add(marker)

        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", 0))
    }

    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(requireContext(), requireContext().getSharedPreferences("osmdroid", 0))
    }
}



package com.example.nammahasiru.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.nammahasiru.R
import com.example.nammahasiru.data.PlantDatabase
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val dao = PlantDatabase.getDatabase(requireContext()).plantDao()
        dao.getAllPlants().observe(viewLifecycleOwner) { plants ->
            googleMap.clear()
            plants.forEach { plant ->
                val pos = LatLng(plant.latitude, plant.longitude)
                val hue = when (plant.status) {
                    "Sprouted" -> BitmapDescriptorFactory.HUE_GREEN
                    "Died"     -> BitmapDescriptorFactory.HUE_RED
                    else       -> BitmapDescriptorFactory.HUE_YELLOW
                }
                googleMap.addMarker(
                    MarkerOptions()
                        .position(pos)
                        .title(plant.speciesName)
                        .snippet(plant.status)
                        .icon(BitmapDescriptorFactory.defaultMarker(hue))
                )
            }
            if (plants.isNotEmpty()) {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(plants[0].latitude, plants[0].longitude), 13f
                    )
                )
            }
        }
    }
}
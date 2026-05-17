package com.example.nammahasiru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        db = AppDatabase.get(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap) {
        map = gMap

        db.dao().getAll().observe(this) { plants ->
            map.clear()
            plants.forEach { plant ->
                val color = when (plant.status) {
                    "Alive" -> BitmapDescriptorFactory.HUE_GREEN
                    "Dead" -> BitmapDescriptorFactory.HUE_RED
                    else -> BitmapDescriptorFactory.HUE_YELLOW
                }

                val position = LatLng(plant.latitude, plant.longitude)
                map.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(plant.species)
                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                )
            }
        }
    }
}

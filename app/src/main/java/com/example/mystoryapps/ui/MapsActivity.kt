package com.example.mystoryapps.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapps.R
import com.example.mystoryapps.databinding.ActivityMapsBinding
import com.example.mystoryapps.db.MyPreferences
import com.example.mystoryapps.viewmodel.HomeViewModel
import com.example.mystoryapps.viewmodel.MainViewModel
import com.example.mystoryapps.viewmodel.RepoViewModelFactory
import com.example.mystoryapps.viewmodel.ViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val homeViewModel: HomeViewModel by viewModels {
        RepoViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val pref = MyPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getToken().observe(this) { token ->
            homeViewModel.getAllStories(token).observe(this){
                for (place in it){
                    val latLng = LatLng(place.lat, place.lon)
                    val yellowMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)

                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(place.name)
                            .snippet(place.description)
                            .icon(yellowMarker)
                    )
                    if (it.indexOf(place) == 1) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }
}
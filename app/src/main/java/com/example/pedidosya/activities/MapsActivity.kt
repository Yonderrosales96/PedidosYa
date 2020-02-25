package com.example.pedidosya.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.pedidosya.R
import com.example.pedidosya.repository.Responses.RestaurantsResponse
import com.example.pedidosya.utils.SharedPreferencesUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.content_maps.*
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var googleMap : GoogleMap
    lateinit var mapFragment: SupportMapFragment
    var data : ArrayList<RestaurantsResponse.restaurantData>? = null
    var textViewVisible : Boolean = false
    var latLng: LatLng? = null
    lateinit var marker: Marker

    companion object{
        val resultActivity : Int = 2
        val requestCode : Int = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(toolbar)
        setTitle("Ubicacion de restaurantes")
        data = SharedPreferencesUtils.getRestaurantes(applicationContext)
        mapFragment = maps as SupportMapFragment
        mapFragment.getMapAsync(this)
        tv_location.setOnClickListener {

            var intent = Intent()
            var locationString = latLng?.latitude.toString()+","+latLng?.longitude.toString()
            intent.putExtra(getString(R.string.location),locationString)
            setResult(resultActivity,intent)
            finish()

        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        if (!data.isNullOrEmpty())
            putMarkers(data!!)
        googleMap.setOnMapClickListener {

            if (!textViewVisible){
                tv_location.visibility = View.VISIBLE
            }
            if (latLng == null) {
                marker = googleMap.addMarker(MarkerOptions().position(it))
            }
            else
                marker.position = it
            latLng = it


        }
    }

    private fun putMarkers(response: ArrayList<RestaurantsResponse.restaurantData>) {
        for (item in response){

            try{
                val auxCoordinates = item.coordinates?.split(Regex(","))
                val latLng = LatLng(auxCoordinates!![0].toDouble(),auxCoordinates[1].toDouble())
                googleMap.addMarker(MarkerOptions().position(latLng))
            }catch (e: Exception){

            }

        }

    }


}

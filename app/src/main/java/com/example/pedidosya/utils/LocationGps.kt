package com.example.pedidosya.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationGps(val activity : Activity,val context: Context,val locationInterface : location) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var requestingLocationUpdates : Boolean = true


    companion object{
        const val REQUEST_PERMISSION_LOCATION: Int = 1
    }

    fun getLastKnownLocation(){

        return if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
                getLocation()
            }else{
                ActivityCompat.requestPermissions(activity,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
            }
        }else{
            getLocation()
        }
    }

    private fun getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener {

            if (it != null)
                locationInterface.successLastKnownLocation(it)
            else
                locationInterface.onErrorFindingLocation("Error")
        }.addOnFailureListener {
            locationInterface.onErrorFindingLocation("error")
        }
    }

    interface location {
        fun successLastKnownLocation(location : Location)
        fun onErrorFindingLocation(error : String)
    }

}
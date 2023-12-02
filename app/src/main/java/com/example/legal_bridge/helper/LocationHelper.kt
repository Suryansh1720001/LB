package com.example.legal_bridge.helper

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException

class LocationHelper(private val context: Context) {

    fun getLatLngFromAddress(addressStr: String): Pair<Double, Double>? {
        val geocoder = Geocoder(context)
        try {
            val addresses: List<Address> = geocoder.getFromLocationName(addressStr, 1)!!
            if (addresses.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                return Pair(latitude, longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    fun getAddressFromLatLng(latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(context)
        try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
            if (addresses.isNotEmpty()) {
                return addresses[0]
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}



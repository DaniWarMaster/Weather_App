package com.example.android.service

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.example.android.service.dao.LatitudeLongitude
import javax.inject.Inject

class GeoLocalization
    @Inject
    constructor(context: Context) {

    private val geocoder : Geocoder = Geocoder(context)

    fun returnAddresse(latLng: LatitudeLongitude): Address {
        val results = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1)
        return results[0]
    }
}
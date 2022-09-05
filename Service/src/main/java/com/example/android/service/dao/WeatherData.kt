package com.example.android.service.dao

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherData(
    val temperatura: Double?,
    val latLng: LatitudeLongitude,
    var isMarker: Boolean,
    val country: String?,
    val city: String?
) : Parcelable

@Parcelize
data class LatitudeLongitude(
    val latitude: Double,
    val longitude: Double,
) : Parcelable

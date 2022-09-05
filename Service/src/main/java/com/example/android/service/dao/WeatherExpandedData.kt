package com.example.android.service.dao

data class WeatherExpandedData (
    val hour: Int,
    val minutes: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val air_pressure_at_sea_level: Double,
    val air_temperature: Double,
    val relative_humidity: Double,
    val wind_speed: Double,
)
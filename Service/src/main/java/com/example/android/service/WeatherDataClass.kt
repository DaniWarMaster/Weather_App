package com.example.android.service

import com.squareup.moshi.Json

data class WeatherDataClass (
    @field:Json(name = "properties") val properties: Properties
)

data class Properties (
    @field:Json(name = "timeseries") val timeseries: List<Timeseries>
)

data class Timeseries (
    @field:Json(name = "time") val time: String,
    @field:Json(name = "data") val data: Data,
)

data class Data (
    @field:Json(name = "instant") val instant: Instant
)

data class Instant(
    @field:Json(name = "details") val details: Details
)

data class Details(
    @field:Json(name = "air_pressure_at_sea_level") val air_pressure_at_sea_level : Double,
    @field:Json(name = "air_temperature") val air_temperature : Double,
    @field:Json(name = "relative_humidity") val relative_humidity : Double,
    @field:Json(name = "wind_speed") val wind_speed : Double,
)
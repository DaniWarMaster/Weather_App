package com.example.android.service

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

interface WeatherApiService {
    @GET("compact")
    suspend fun getProperties(@Header("User-Agent") appName : String, @Query("lat") lat : Double, @Query("lon") lon : Double):
    //Call<String>
            WeatherDataClass
}

interface WeatherAPIHelper {
    suspend fun getProperties(string: String, lat: Double, lon: Double) : WeatherDataClass
}

class WeatherAPIHelperImpl
@Inject
constructor(private val weatherApiService: WeatherApiService) : WeatherAPIHelper {
    override suspend fun getProperties(string: String, lat: Double, lon: Double): WeatherDataClass = weatherApiService.getProperties(string,lat,lon)
}


class WeatherApi
@Inject
constructor(private val weatherAPIHelper: WeatherAPIHelper){
    suspend fun getProperties(string: String, lat: Double, lon: Double) = weatherAPIHelper.getProperties(string,lat,lon)
}
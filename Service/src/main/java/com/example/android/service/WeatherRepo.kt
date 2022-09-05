package com.example.android.service

import android.location.Address
import android.util.Log
import com.example.android.database.WeatherDatabaseDataClass
import com.example.android.service.dao.LatitudeLongitude
import com.example.android.service.dao.WeatherAddressData
import com.example.android.service.dao.WeatherData
import com.example.android.service.dao.WeatherExpandedData
import java.lang.Exception
import javax.inject.Inject
import kotlin.collections.ArrayList

class WeatherRepo
@Inject
constructor(
    private val weatherApi: WeatherApi,
    private val databaseRequestHandler: DatabaseRequestHandler,
    private val geoLocalization: GeoLocalization
    ) {

    /// some string constants
    private val stringDATA : StringData = StringData()

    /// return weather temperature at target location
    suspend fun getTemperatureFromLocation(latLng : LatitudeLongitude) : Double {
        val response = weatherApi.getProperties(stringDATA.appName, latLng.latitude, latLng.longitude)
        Log.d("WeatherRepo", "getWeatherData: ${response.properties.timeseries[0].data.instant.details.air_temperature}")
        return response.properties.timeseries[0].data.instant.details.air_temperature
    }

    /// return all info regarding the weather at target location
    /// @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getExpandedWeatherInfo(latitude: Double, longitude: Double): ArrayList<WeatherExpandedData> {
        val latLng = LatitudeLongitude(latitude, longitude)
        val response = weatherApi.getProperties(stringDATA.appName, latLng.latitude, latLng.longitude)
        Log.d("WeatherRepo", "getWeatherData_RawData: $response")

        //var time = DateTimeFormatter("yyyy-MM-ddTHH:mm:ssZ")
        val time = response.properties.timeseries[0].time
        var hour = Integer.valueOf("" + time[11] + time[12])

        var year = Integer.valueOf("" + time[0] + time[1] + time[2] + time[3])
        var month = Integer.valueOf("" + time[5] + time[6])
        var day = Integer.valueOf("" + time[8] + time[9])

        val result : ArrayList<WeatherExpandedData> = ArrayList()

        response.properties.timeseries.forEach {
            val item = WeatherExpandedData(
                hour,
                0,
                day,
                month,
                year,
                it.data.instant.details.air_pressure_at_sea_level,
                it.data.instant.details.air_temperature,
                it.data.instant.details.relative_humidity,
                it.data.instant.details.wind_speed,
            )
            result.add(item)

            val timeAct = it.time
            val yearAct = Integer.valueOf("" + timeAct[0] + timeAct[1] + timeAct[2] + timeAct[3])
            val monthAct = Integer.valueOf("" + timeAct[5] + timeAct[6])
            val dayAct = Integer.valueOf("" + timeAct[8] + timeAct[9])

            hour++
            if(dayAct != day) {
                day = dayAct
                hour = 0
            }

            if(monthAct != month) {
                month = monthAct
            }

            if(yearAct != year) {
                year = yearAct
            }
        }

        return result
    }

    /// insert new weather location into the database
    fun insertWeatherData(weatherDatabaseDataClass: WeatherDatabaseDataClass) {
        databaseRequestHandler.insertIntoDatabase(weatherDatabaseDataClass)
    }

    /// return WeatherData from a given langitude and latitude
    suspend fun getWeatherDataFromPoint(latLng: LatitudeLongitude, isMarker : Boolean) : WeatherData {
        val temperature = getTemperatureFromLocation(latLng)
        val adress = getLocationAddress(latLng)

        //// insert into database
        val item = WeatherDatabaseDataClass(
            0,
            latLng.latitude,
            latLng.longitude,
            adress.country,
            adress.city
        )
        insertWeatherData(item)

        return WeatherData(
            temperature,
            LatitudeLongitude(latLng.latitude, latLng.longitude),
            isMarker,
            adress.country,
            adress.city
        )
    }

    /// get all weather data from the database - raw
    suspend fun getAllWeatherDataFromDB(): ArrayList<WeatherData> {
        val partialResult = databaseRequestHandler.getAllWeatherData()
        val result : ArrayList<WeatherData> = ArrayList()

        partialResult.forEach {
            val loc = LatitudeLongitude(it.latitude, it.longitude)
            val temperature = getTemperatureFromLocation(loc)
            result.add(WeatherData(temperature,loc,false,it.country,it.city))
        }

        return result
    }

    /// return all weather location from the database + details about them
    suspend fun getAllWeatherData() : ArrayList<WeatherData> {
        val partialResult = databaseRequestHandler.getAllWeatherData()
        val finalResult : ArrayList<WeatherData> = ArrayList()

        partialResult.forEach {
            finalResult.add(getPointerFromDatabase(LatitudeLongitude(it.latitude,it.longitude)))
        }

        return finalResult
    }

    /// return adress details -> (Country, City) -> for a location
    fun getLocationAddress(latLng: LatitudeLongitude) : WeatherAddressData {
        val adress : Address
        var countryName = ""
        var cityName = ""
        try {
            adress = geoLocalization.returnAddresse(latLng)
            Log.d("WeatherRepo", "getLocationAddress: ${adress.countryName} - ${adress.locality}")
            if(adress.countryName != null) {
                countryName = adress.countryName
            }
            else {
                countryName = "unknown"
            }

            if(adress.locality != null) {
                cityName = adress.locality
            }
            else {
                cityName = "unknown"
            }
        }
        catch (e : Exception) {
            println(e.message)
            println(e.printStackTrace())
        }

        return WeatherAddressData(
            countryName,
            cityName
        )
    }

    /// return a data set for a location from the database
    suspend fun getPointerFromDatabase(latLng: LatitudeLongitude) : WeatherData {
        val temperature = getTemperatureFromLocation(latLng)

        val address = getLocationAddress(latLng)

        return WeatherData(
            temperature,
            latLng,
            false,
            address.country,
            address.city
        )
    }
}
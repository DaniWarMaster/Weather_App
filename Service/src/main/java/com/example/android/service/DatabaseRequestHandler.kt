package com.example.android.service

import com.example.android.database.WeatherDatabaseDAO
import com.example.android.database.WeatherDatabaseDataClass
import javax.inject.Inject

class DatabaseRequestHandler
@Inject
constructor(
    private val weatherDatabaseDAO: WeatherDatabaseDAO
) {

    fun insertIntoDatabase(item: WeatherDatabaseDataClass) {
        weatherDatabaseDAO.insert(item)
    }

    fun getAllWeatherData(): List<WeatherDatabaseDataClass> {
        return weatherDatabaseDAO.getAll()
    }
}
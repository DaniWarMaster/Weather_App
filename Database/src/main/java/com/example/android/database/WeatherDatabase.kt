package com.example.android.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherDatabaseDataClass::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val weatherDatabaseDAO : WeatherDatabaseDAO
}
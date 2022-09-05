package com.example.android.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeatherDatabaseDAO {
    @Insert
    fun insert(weatherDatabaseDataClass: WeatherDatabaseDataClass)

    @Update
    fun update(weatherDatabaseDataClass: WeatherDatabaseDataClass)

    @Query("SELECT * from weather_data_table WHERE weatherID = :key")
    fun get(key: Long): WeatherDatabaseDataClass

    @Query("SELECT * from weather_data_table ORDER BY weatherID ASC")
    fun getAll(): List<WeatherDatabaseDataClass>
}
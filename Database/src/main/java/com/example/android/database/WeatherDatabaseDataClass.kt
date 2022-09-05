package com.example.android.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data_table")
data class WeatherDatabaseDataClass (

    @PrimaryKey(autoGenerate = true)
    var weatherID: Long = 0L,

    @ColumnInfo(name = "latitude")
    var latitude: Double = 0.0,

    @ColumnInfo(name = "longitude")
    var longitude: Double = 0.0,

    @ColumnInfo(name = "country")
    var country: String?,

    @ColumnInfo(name = "city")
    var city: String?
)
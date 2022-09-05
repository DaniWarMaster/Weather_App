package com.example.android.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.android.database.WeatherDatabase
import com.example.android.database.WeatherDatabaseDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): WeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Provides
    fun provideChannelDao(weatherDatabase: WeatherDatabase): WeatherDatabaseDAO {
        return weatherDatabase.weatherDatabaseDAO
    }
}
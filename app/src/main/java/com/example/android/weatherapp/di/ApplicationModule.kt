package com.example.android.weatherapp.di

import android.content.Context
import com.example.android.database.WeatherDatabase
import com.example.android.service.DatabaseRequestHandler
import com.example.android.service.WeatherApi
import com.example.android.service.WeatherRepo
import com.example.android.service.GeoLocalization
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Singleton
    @Provides
    fun provideWeatherRepo(weatherApi: WeatherApi, databaseRequestHandler: DatabaseRequestHandler, geoLocalization: GeoLocalization) : WeatherRepo {
        return WeatherRepo(weatherApi, databaseRequestHandler, geoLocalization)
    }

    @Singleton
    @Provides
    fun provideDatabaseRequestHandler(weatherDatabase: WeatherDatabase) : DatabaseRequestHandler {
        return DatabaseRequestHandler(weatherDatabase.weatherDatabaseDAO)
    }

    @Singleton
    @Provides
    fun provideGeoLocalization(@ApplicationContext context: Context) : GeoLocalization {
        return GeoLocalization(context)
    }
}
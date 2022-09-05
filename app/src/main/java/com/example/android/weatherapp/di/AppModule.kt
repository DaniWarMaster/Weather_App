package com.example.android.weatherapp.di

import com.example.android.details.WeatherDetailsData
import com.example.android.service.dao.WeatherData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @ViewModelScoped
    @Provides
    fun provideBooleanVariable() : Boolean {
        return false
    }

    @ViewModelScoped
    @Provides
    fun provideArrayListWeatherData() : ArrayList<WeatherData> {
        return ArrayList()
    }

    @ViewModelScoped
    @Provides
    fun provideArrayListWeatherDetailsData() : ArrayList<WeatherDetailsData> {
        return ArrayList()
    }
}
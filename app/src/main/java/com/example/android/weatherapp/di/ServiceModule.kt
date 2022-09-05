package com.example.android.weatherapp.di

import com.example.android.service.WeatherAPIHelper
import com.example.android.service.WeatherAPIHelperImpl
import com.example.android.service.WeatherApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Singleton
    @Provides
    fun provideMoshiBuilder() = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideRetrofitBuilder(moshi: Moshi) = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.met.no/weatherapi/locationforecast/2.0/")
        .build()

    @Singleton
    @Provides
    fun provideWeatherAPIService(retrofit: Retrofit) = retrofit.create(WeatherApiService::class.java)

    @Singleton
    @Provides
    fun provideWeatherAPIHelper(apiHelper: WeatherAPIHelperImpl) : WeatherAPIHelper = apiHelper
}
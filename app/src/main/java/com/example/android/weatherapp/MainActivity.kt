package com.example.android.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android.view.MapFragment
import com.example.android.service.dao.WeatherData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MapFragment.OpenDetailsListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val f : Fragment? = supportFragmentManager.findFragmentById(R.id.myNavHostFragment)
        Log.d("MainActivity: ", "onCreate: Map fragment variable in MainActivity: $f")
    }

    /// move from map fragment to details fragment
    override fun onAction(view : View, weatherData: WeatherData) {
        val bundle = bundleOf("data" to weatherData)
        Navigation.findNavController(view).navigate(R.id.action_mapFragment_to_detailsFragment, bundle)
    }
}
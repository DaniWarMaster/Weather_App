package com.example.android.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.android.service.dao.WeatherData
import com.example.android.view.uiStuff.Purple
import com.example.android.view.uiStuff.Purple_Dark
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels()

    /// map related variables
    private lateinit var mMap : GoogleMap
    //private lateinit var mapFragment : SupportMapFragment
    //private lateinit var binding : MainMapFragmentBinding

    /// adapter for the list displayed in the main fragment
    //private lateinit var adapter : WeatherLocationAdapter

    /// open details view for weather location -- listener
    private lateinit var openDetailsListener: OpenDetailsListener
    interface OpenDetailsListener {
        fun onAction(view: View, weatherData: WeatherData)
    }
    fun setOpenDetailsListener(openDetailsListener: OpenDetailsListener) {
        this.openDetailsListener = openDetailsListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        //binding = DataBindingUtil.inflate(inflater, R.layout.main_map_fragment, container, false)

        val onMapRdCallback = this
        return ComposeView(requireContext()).apply {
            setContent {
                MapFragmentLayoutInitializer(viewModel = viewModel, onMapReadyCallback = onMapRdCallback)
            }
        }

        //return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MapFragment", "onViewCreated - getAllWeatherData")
        viewModel.setUiEvent(MapEventState.Event.InitializeMapData)


        setOpenDetailsListener(activity as OpenDetailsListener)
    }

    @Composable
    fun MapFragmentLayoutInitializer(viewModel: MapViewModel, onMapReadyCallback: OnMapReadyCallback) {
        val state by viewModel.uiState.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            BuildMap(onMapReadyCallback = onMapReadyCallback)

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.White)
                .align(Alignment.BottomCenter)) {

                BuildWeatherList(
                    state.locationList,
                    state.isDataLoaded,
                    state.isMapLoaded
                )
            }
        }
    }
    
    @Composable
    fun BuildMap(onMapReadyCallback: OnMapReadyCallback) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                layoutInflater.inflate(R.layout.main_map_fragment, null, false)
            },
            update = {
                (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).apply {
                    getMapAsync(onMapReadyCallback)
                }
            }
        )

    }

    @Composable
    fun BuildWeatherList(
        locationList: ArrayList<WeatherData>,
        dataLoaded: Boolean,
        mapLoaded: Boolean
    ) {
        if(!dataLoaded) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                CircularProgressIndicator()
            }
        }
        else {
            if(mapLoaded) {
                checkMapPointers(mMap)
            }
        }

        ///var 2
        /*
        LazyRow() {
            items(locationList) { weatherData ->
                WeatherDataCardBox(weatherData)

                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
         */

        ///var 1
        LazyColumn {
            items(locationList) { weatherData ->
                WeatherDataCard(weatherData)

                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }

    @Composable
    fun WeatherDataCardBox(weatherData: WeatherData) {
        Column(modifier = Modifier
            .clickable { view?.let { openDetailsListener.onAction(it, weatherData = weatherData) } }
            .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {

            Icon(painter =
                when {
                    weatherData.temperatura!! > 20.0 -> painterResource(id = R.drawable.ic_baseline_wb_sunny_24)
                    weatherData.temperatura!! > 5.0 -> painterResource(id = R.drawable.ic_baseline_cloud_24)
                    else -> painterResource(id = R.drawable.ic_baseline_ac_unit_24)
                },
                contentDescription = null,
                tint = Purple_Dark,
                modifier = Modifier
                    .padding(start = 2.dp, end = 6.dp)
                    .align(CenterHorizontally)
            )

            Column {
                Text(text = weatherData.city.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)

                Text(text = weatherData.country.toString(),
                    fontSize = 16.sp,
                    color = Color.Gray)
            }

            Text(text = weatherData.temperatura.toString() + "°C",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Purple)
        }
    }

    @Composable
    fun WeatherDataCard(weatherData: WeatherData) {
        Row(modifier = Modifier
            .clickable { view?.let { openDetailsListener.onAction(it, weatherData = weatherData) } }
            .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {

            Icon(painter =
                when {
                    weatherData.temperatura!! > 20.0 -> painterResource(id = R.drawable.ic_baseline_wb_sunny_24)
                    weatherData.temperatura!! > 5.0 -> painterResource(id = R.drawable.ic_baseline_cloud_24)
                    else -> painterResource(id = R.drawable.ic_baseline_ac_unit_24)
                },
                contentDescription = null,
                tint = Purple_Dark,
                modifier = Modifier
                    .padding(start = 2.dp, end = 6.dp)
                    .align(CenterVertically)
                )

            Column {
                Text(text = weatherData.city.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)

                Text(text = weatherData.country.toString(),
                    fontSize = 16.sp,
                    color = Color.Gray)
            }
            
            Text(text = weatherData.temperatura.toString() + "°C",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Purple)
        }
    }

    override fun onResume() {
        super.onResume()
        //adapter.updateDataList(viewModel.uiState.value.locationList)
        //adapter.notifyDataSetChanged()
        Log.d("MapFragment", "onResume: MapFragmentResumed ${viewModel.uiState.value.locationList}")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("MapFragment", "onMapReady: -->  Am intrat in mapa")

        viewModel.setUiEvent(MapEventState.Event.MapLoaded)

        /// set listener for map and handle click events
        mMap.setOnMapLongClickListener {
            Log.d("MapFragment: ","onMapReady: Am apasat lung pe mapa: ${it.latitude}  - ${it.longitude}")

            mMap.addMarker(MarkerOptions().position(it).title("LocatiionMap ${it.longitude} ${it.latitude}"))

            Log.d("MapFragment", "onMapReady: EventTriggered")
            viewModel.setUiEvent(MapEventState.Event.AddMapPoint(it,true))

            mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
        }
    }

    /// check the existence of markers on the map for each selected location from the map
    private fun checkMapPointers(map: GoogleMap) {
        //map.clear()
        Log.d("MapFragment", "checkMapPointers: entered checking function")
        viewModel.uiState.value.locationList.forEach {
            if(!it.isMarker) {
                Log.d("MapFragment", "checkMapPointers: added marker at ${it.country}-${it.city}")
                lifecycleScope.launch(Dispatchers.Main) {
                    map.addMarker(MarkerOptions().position(LatLng(it.latLng.latitude,it.latLng.longitude)).title("LocatiionMap ${it.latLng.longitude} ${it.latLng.latitude}"))
                    //it.isMarker = true
                }
            }
        }
        viewModel.setUiEvent(MapEventState.Event.PointersAddedOnMap)
    }
}
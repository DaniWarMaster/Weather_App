package com.example.android.view

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.android.common_view.BaseViewModel
import com.example.android.service.WeatherRepo
import com.example.android.service.dao.LatitudeLongitude
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel
@Inject
constructor(
    private val weatherRepo: WeatherRepo,
) : BaseViewModel<MapEventState.Event, MapEventState.State, MapEventState.Effect>() {

    //var locationList: ArrayList<WeatherData> = ArrayList()

    override fun createInitialState(): MapEventState.State {
        return MapEventState.State(MapEventState.MapState.Loading, ArrayList(),
            isDataLoaded = false,
            isMapLoaded = false
        )
    }

    override fun handleEvent(event: MapEventState.Event) {
        when(event) {
            is MapEventState.Event.InitializeMapData -> {
                if(uiState.value.isDataLoaded) {
                    setUiState { copy(mapState = MapEventState.MapState.Idle) }
                }
                else {
                    getAllWeatherData()
                }
            }

            is MapEventState.Event.AddMapPoint -> {
                Log.d("MapViewModel", "onAddMapPointEvent: Event Received")
                addPointer(event.latLng, event.marker)
            }

            //// if map is loaded and data is loaded we display the data else we wait
            is MapEventState.Event.MapLoaded -> {
                setUiState {
                    copy(isMapLoaded = true)
                }
                if (currentState.isDataLoaded) {
                    setUiState {
                        copy(mapState = MapEventState.MapState.LoadData)
                    }
                }
            }

            //// if data is loaded and map is loaded we display the data esle we wait
            is MapEventState.Event.DataLoaded -> {
                setUiState {
                    copy(isDataLoaded = true)
                }
                if (currentState.isMapLoaded) {
                    setUiState {
                        copy(mapState = MapEventState.MapState.LoadData)
                    }
                }
            }

            is MapEventState.Event.FinishedLoadingData -> {
                setUiState { copy(mapState = MapEventState.MapState.Idle) }
            }

            is MapEventState.Event.PointersAddedOnMap -> {
                val partialResult = uiState.value.locationList
                partialResult.forEach {
                    it.isMarker = true
                }
                setUiState { copy(locationList = partialResult) }
            }
        }
    }

    /// get al data from database
    fun getAllWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            val partialResult = weatherRepo.getAllWeatherData()

            Log.d("MapViewModel", "getAllWeatherData: $partialResult")

            if(partialResult!=null) {
                setUiState {
                    copy(locationList = partialResult)
                }
            }

            setUiEvent(MapEventState.Event.DataLoaded)
        }
    }

    /// handle adding a new location event
    fun addPointer(latLng: LatLng, marker: Boolean) {

        viewModelScope.launch(Dispatchers.IO) {
            setUiState { copy(mapState = MapEventState.MapState.Loading, isDataLoaded = false) }

            val pointMap = weatherRepo.getWeatherDataFromPoint(LatitudeLongitude(latLng.latitude, latLng.longitude), marker)
            val partialResult = uiState.value.locationList
            partialResult.add(pointMap)

            setUiState { copy(mapState = MapEventState.MapState.Success, locationList = partialResult, isDataLoaded = true) }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
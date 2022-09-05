package com.example.android.view

import com.example.android.common_view.UiEffect
import com.example.android.common_view.UiEvent
import com.example.android.common_view.UiState
import com.example.android.service.dao.WeatherData
import com.google.android.gms.maps.model.LatLng

class MapEventState {
    sealed class Event : UiEvent {
        class AddMapPoint(
            val latLng: LatLng,
            val marker: Boolean,
        ) : Event()
        object MapLoaded : Event()
        object DataLoaded : Event()
        object FinishedLoadingData : Event()
        object PointersAddedOnMap : Event()
        object InitializeMapData : Event()
    }

    data class State(
        val mapState : MapState,
        val locationList : ArrayList<WeatherData> = ArrayList(),
        val isDataLoaded : Boolean = false,
        val isMapLoaded : Boolean = false,
    ) : UiState

    sealed class MapState {
        object Idle : MapState()
        object Loading : MapState()
        object LoadData : MapState()
        object Success : MapState()
    }
    sealed class Effect : UiEffect
}
package com.example.android.details

import com.example.android.common_view.UiEffect
import com.example.android.common_view.UiEvent
import com.example.android.common_view.UiState
import com.example.android.service.dao.WeatherExpandedData

class DetailsEventState {
    sealed class Event : UiEvent {
        data class LoadData(
            val latitude : Double,
            val longitute : Double,
        ) : Event()
    }

    data class State(
        val detailsState: DetailsState,
        val weatherExpandedToday: ArrayList<WeatherExpandedData> = ArrayList(),
        val weatherExpandedWeek: ArrayList<WeatherExpandedData> = ArrayList(),
        val isDataLoaded: Boolean = false,
    ) : UiState

    sealed class DetailsState {
        object Loading : DetailsState()
        object Success : DetailsState()
    }

    sealed class Effect : UiEffect
}
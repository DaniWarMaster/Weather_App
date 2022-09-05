package com.example.android.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.android.common_view.BaseViewModel
import com.example.android.service.WeatherRepo
import com.example.android.service.dao.WeatherData
import com.example.android.service.dao.WeatherExpandedData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject
constructor(
    var arrayList : ArrayList<WeatherDetailsData>,
    private val weatherRepo: WeatherRepo,
    savedStateHandle: SavedStateHandle
)
    : BaseViewModel<DetailsEventState.Event, DetailsEventState.State, DetailsEventState.Effect>() {

    var weatherData : WeatherData? = savedStateHandle.get<WeatherData>("data")
    //var expandedWeatherDataToday : MutableList<WeatherExpandedData> = mutableListOf()
    //var expandedWeatherDataWeek : MutableList<WeatherExpandedData> = mutableListOf()
    //var expandedWeatherData : ArrayList<WeatherExpandedData> = ArrayList()

    init {
        Log.d("DetailsViewModle", "init: am gasit datele in details fragment $weatherData")

        viewModelScope.launch {
            setUiEvent(DetailsEventState.Event.LoadData(weatherData!!.latLng.latitude, weatherData!!.latLng.longitude))
        }
    }

    fun getWeatherExpandedData(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val rezult = weatherRepo.getExpandedWeatherInfo(latitude, longitude)
            val expandedWeatherDataToday = ArrayList<WeatherExpandedData>()
            val expandedWeatherDataWeek = ArrayList<WeatherExpandedData>()

            val today = rezult[0].day
            for (i in 0..rezult.size - 1) {
                if (rezult[i].day == today) {
                    expandedWeatherDataToday.add(rezult[i])
                } else {
                    break
                }
            }
            //expandedWeatherDataToday = mutableListOf(expandedWeatherDataToday[0])

            var thisWeek = rezult[0].day + rezult[0].month + rezult[0].year
            expandedWeatherDataWeek.add(rezult[0])
            for (i in 0..rezult.size - 1) {
                if (rezult[i].day + rezult[i].month + rezult[i].year != thisWeek) {
                    thisWeek = rezult[i].day + rezult[i].month + rezult[i].year
                    expandedWeatherDataWeek.add(rezult[i])
                }
            }

            setUiState {
                copy(
                    detailsState = DetailsEventState.DetailsState.Success,
                    weatherExpandedToday = expandedWeatherDataToday,
                    weatherExpandedWeek = expandedWeatherDataWeek,
                    true
                )
            }
            Log.d("DetailsViewModel", "getWeatherExpandedData: ${uiState.value.isDataLoaded}")
        }
    }

    override fun createInitialState(): DetailsEventState.State {
        return DetailsEventState.State(DetailsEventState.DetailsState.Loading, ArrayList(), ArrayList(),false)
    }

    override fun handleEvent(event: DetailsEventState.Event) {
        when(event) {
            is DetailsEventState.Event.LoadData -> {
                getWeatherExpandedData(event.latitude, event.longitute)
            }
        }
    }
}
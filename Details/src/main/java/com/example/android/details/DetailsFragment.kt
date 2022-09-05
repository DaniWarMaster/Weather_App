package com.example.android.details

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.example.android.details.uiStuff.Purple
import com.example.android.details.uiStuff.Shapes
import com.example.android.service.dao.WeatherData
import com.example.android.service.dao.WeatherExpandedData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: Fragment() {
    private val viewModel: DetailsViewModel by viewModels()
    //private lateinit var binding: DetailsLayoutBinding
    private lateinit var adapter: WeatherDetailsAdapter


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {

        return ComposeView(requireContext()).apply {
            setContent {
                WeatherFragmentLayoutInitializer(viewModel = viewModel)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WeatherDetailsAdapter(viewModel.arrayList)

    }

    @Composable
    fun WeatherFragmentLayoutInitializer(viewModel: DetailsViewModel) {
        //val state: DetailsEventState.State = viewModel.currentState

        val state by viewModel.uiState.collectAsState()

        WeatherFragmentLayout(
            weatherData = viewModel.weatherData,
            weatherExpandedDataToday = state.weatherExpandedToday,
            weatherExpandedDataWeek = state.weatherExpandedWeek,
            isLoaded = state.isDataLoaded
        )
    }

    @Composable
    fun WeatherFragmentLayout(weatherData: WeatherData?, weatherExpandedDataToday: ArrayList<WeatherExpandedData>, weatherExpandedDataWeek: ArrayList<WeatherExpandedData>, isLoaded : Boolean) {
        var isExpanded by remember {
            mutableStateOf(false)
        }

        Column(modifier = Modifier.padding(all = 4.dp)) {
            Column(modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(all = 8.dp)
                .padding(bottom = 40.dp)
            ) {
                LocationCard(city = weatherData!!.city, country = weatherData.country, isExpanded)

                Spacer(modifier = Modifier.height(8.dp))

                TemperatureCard(temperature = weatherData.temperatura)
            }

            Log.d("DetailsFragment-Compose", "WeatherFragmentLayout: $isLoaded")
            if(!isLoaded) {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    CircularProgressIndicator()
                }
            }

            LazyRow {
                items(weatherExpandedDataToday) { temp ->
                    TemperatureListItem(temp.air_temperature, temp.hour, temp.minutes)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))


            LazyColumn(
                modifier = Modifier
                    .padding(all = 5.dp)
                    .padding(top = 10.dp, bottom = 10.dp)
            ) {
                items(weatherExpandedDataWeek) { data ->
                    WeatherListItem(weatherExpandedData = data)

                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }

        }
    }

    @Composable
    fun TemperatureListItem(temperature: Double, hour: Int, minutes: Int) {
        Column(
            Modifier
                .padding(start = 4.dp, top = 1.dp, end = 4.dp, bottom = 1.dp)
                .border(1.dp, color = Color.Gray, shape = Shapes.small)
                .padding(all = 4.dp)
        ) {
            Text(
                text =  (if(hour < 10) "0$hour" else hour.toString())
                        + ":" +
                        (if(minutes < 10) "0$minutes" else minutes.toString()),
                modifier = Modifier
                    .width(50.dp)
                    .padding(1.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
            )
            Text(
                text = "$temperature°C",
                modifier = Modifier
                    .width(50.dp)
                    .padding(2.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Purple,
            )
        }
    }

    @Composable
    fun WeatherListItem(weatherExpandedData: WeatherExpandedData) {
        var isExpanded by remember {
            mutableStateOf(false)
        }

        Row(modifier = Modifier
            .clickable { isExpanded = !isExpanded }
            .padding(all = 8.dp)
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text =  (if(weatherExpandedData.day < 10) "0" else "") + weatherExpandedData.day.toString()
                            + "/" +
                            (if(weatherExpandedData.month < 10) "0" else "") + weatherExpandedData.month.toString()
                            + "/" +
                            weatherExpandedData.year.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.height(4.dp))

                if(isExpanded) {
                    Text(
                        text = "Air Pressure: ${weatherExpandedData.air_pressure_at_sea_level}",
                        modifier = Modifier.padding(all = 4.dp),
                        fontSize = 14.sp,
                    )

                    Text(
                        text = "Humidity: ${weatherExpandedData.relative_humidity}",
                        modifier = Modifier.padding(all = 4.dp),
                        fontSize = 14.sp,
                    )

                    Text(
                        text = "Wind Speed: ${weatherExpandedData.wind_speed}",
                        modifier = Modifier.padding(all = 4.dp),
                        fontSize = 14.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text (
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = weatherExpandedData.air_temperature.toString() + "°C",
                fontSize = 25.sp,
                color = Purple,
            )
        }
    }

    @Composable
    fun LocationCard(city: String?, country: String?, expandedVar: Boolean) {
        Text(
            text = city + "" ,
            modifier = Modifier
                .padding(top = 25.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            color = Color.Gray,
        )
        if(expandedVar) {
            Text(
                text = country + "" ,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.Gray,
            )
        }
    }

    @Composable
    fun TemperatureCard(temperature: Double?) {
        Text(
            text = temperature.toString() + "°C",
            modifier = Modifier.fillMaxWidth(),
            color = Purple,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}
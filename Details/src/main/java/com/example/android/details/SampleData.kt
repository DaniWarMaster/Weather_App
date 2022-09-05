import com.example.android.details.WeatherDetailsData

data class SampleData(
    val isActive: Boolean,
    val resultResponse: String,
    val errorNumber: Int,
    val weatherDetailsData1: WeatherDetailsData,
    val weatherDetailsData2: WeatherDetailsData,
)
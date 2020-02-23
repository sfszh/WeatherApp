package co.ruizhang.weatherapp.business

data class CityWeather(
    val id: Int,
    val name: String,
    val weather: String
)


data class CityDetailModel(
    val id: Int,
    val name: String,
    val weathers: List<String>
)


data class SelectedCityModel(
    val id: String,
    val name: String,
    val country: String
)
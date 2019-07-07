package co.ruizhang.weatherapp.business

 data class CityModel (
    val id : Int,
    val name: String,
    val weather: String
)


data class CityDetailModel (
    val id : Int,
    val name : String,
    val weathers: List<String>
)
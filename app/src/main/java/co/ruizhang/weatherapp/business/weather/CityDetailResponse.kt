package co.ruizhang.weatherapp.business.weather

import co.ruizhang.weatherapp.business.CityDetailModel
import com.google.gson.annotations.Expose

open class CityDetailResponse {
    @Expose
    var list: List<WeatherInfo> = emptyList()
    @Expose
    var city: CityInfo? = null

    fun mapToDomain(): CityDetailModel {
        val weathers = list.mapNotNull { it.weather.firstOrNull() }.mapNotNull { it.main }
        return CityDetailModel(city?.id ?: 0, city?.name ?: "empty", weathers)
    }
}

data class WeatherInfo(
    val weather: List<WeatherEntity>
)

data class CityInfo(
    val id: Int,
    val name: String,
    val country: String
)
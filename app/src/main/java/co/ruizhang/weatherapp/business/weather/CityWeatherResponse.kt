package co.ruizhang.weatherapp.business.weather

import co.ruizhang.weatherapp.business.CityModel
import com.google.gson.annotations.Expose

open class CityWeatherResponse {
    @Expose
    var cont: Int? = null
    @Expose
    var list: List<CityEntity> = emptyList()

    fun mapToDomain(): List<CityModel> {
        return list.mapNotNull { city ->
            val weather = city.weather?.first() ?: return@mapNotNull null
            CityModel(city.id, city.name ?: "empty", weather.main ?: "empty")
        }
    }
}

open class CityEntity {
    @Expose
    var weather: List<WeatherEntity>? = null

    @Expose
    var id: Int = 0
    @Expose
    var name: String? = null
}


open class WeatherEntity {
    @Expose
    var id: Int? = null
    @Expose
    var main: String? = null
    @Expose
    var description: String? = null
    @Expose
    var icon: String? = null
}



package co.ruizhang.weatherapp.business

import co.ruizhang.weatherapp.business.persistence.CityWeatherDBModel
import co.ruizhang.weatherapp.business.persistence.WeatherDatabase
import io.reactivex.Single

interface CityWeatherStorage {

    fun getAllWeather(): Single<List<CityWeatherDBModel>>

    fun insert(weather: List<CityWeatherDBModel>): Single<List<CityWeatherDBModel>>

    fun deleteAllWeather()
}


class CityWeatherStorageImpl constructor(private val database: WeatherDatabase) :
    CityWeatherStorage {
    override fun getAllWeather(): Single<List<CityWeatherDBModel>> {
        return database.getCityWeatherDao().getAllWeather()
    }

    override fun insert(weather: List<CityWeatherDBModel>): Single<List<CityWeatherDBModel>> {
        return database.getCityWeatherDao().insert(weather)
            .andThen(database.getCityWeatherDao().getAllWeather())
    }

    override fun deleteAllWeather() {
        return database.getCityWeatherDao().deleteAllWeather()
    }
}
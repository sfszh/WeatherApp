package co.ruizhang.weatherapp.business

import co.ruizhang.weatherapp.BuildConfig
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface CityDetailRepository {
    fun get(id: Int, useCache: Boolean): Single<CityDetailModel>
}

class CityDetailRepositoryImpl(private val weatherApi: WeatherApi) : CityDetailRepository {
    override fun get(id: Int, useCache: Boolean): Single<CityDetailModel> {
        return weatherApi
            .forcast(id, 5, getApiKey())
            .subscribeOn(Schedulers.io())
            .map { resp -> resp.mapToDomain() }
    }

    private fun getApiKey(): String {
        return BuildConfig.WEATHER_API_KEY
    }
}
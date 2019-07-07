package co.ruizhang.weatherapp.business

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface CityListRepository {
    fun getCities(useCache: Boolean): Single<List<CityModel>>
    fun get(id: Int, useCache: Boolean): Single<CityDetailModel>
}

class CityListRepositoryImpl(private val weatherApi: WeatherApi) : CityListRepository {
    override fun getCities(useCache: Boolean): Single<List<CityModel>> {
        return weatherApi
            .weather(getCityIds().joinToString(","), "metrics", getApiKey())
            .subscribeOn(Schedulers.io())
            .map { weather -> weather.mapToDomain() }
    }

    override fun get(id: Int, useCache: Boolean): Single<CityDetailModel> {
        return weatherApi
            .forcast(id, 5, getApiKey())
            .subscribeOn(Schedulers.io())
            .map { resp -> resp.mapToDomain() }
    }

    //todo retrieve this from keystore
    private fun getApiKey(): String {
        return "eee56fea6143a11bbf946a0273cc5b81"
    }

    //todo retrieve this from storage
    private fun getCityIds(): List<Int> {
        return listOf(524901,703448,2643743)
    }

}
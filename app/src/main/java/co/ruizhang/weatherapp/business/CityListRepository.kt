package co.ruizhang.weatherapp.business

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface CityListRepository {
    fun getCities(useCache: Boolean): Single<List<CityModel>>
}

class CityListRepositoryImpl(private val weatherApi: WeatherApi) : CityListRepository {
    override fun getCities(useCache: Boolean): Single<List<CityModel>> {
//        return Single.just(listOf(CityModel("cityid", "cityName", "cityWeather")))
        return weatherApi
            .weather(getCityIds(), "metrics", getApiKey())
            .subscribeOn(Schedulers.io())
            .map { weather -> weather.mapToDomain() }
    }

    //todo retrieve this from keystore
    private fun getApiKey(): String {
        return "eee56fea6143a11bbf946a0273cc5b81"
    }

    //todo retrieve this from storage
    private fun getCityIds(): List<String> {
        return listOf("524901")
    }

}
package co.ruizhang.weatherapp.business

import io.reactivex.Single

interface CityListService {
    fun getCities(useCache: Boolean): Single<List<CityModel>>
}

class CityListServiceImpl : CityListService {
    override fun getCities(useCache: Boolean): Single<List<CityModel>> {
        return Single.just(listOf(CityModel("cityid", "cityName", "cityWeather")))
    }
}
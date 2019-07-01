package co.ruizhang.weatherapp.business

import io.reactivex.Single

interface CityListService {
    fun getCitys() : Single<List<CityModel>>
}

class CityListServiceImpl : CityListService {
    override fun getCitys(): Single<List<CityModel>> {
        return Single.just(listOf(CityModel("cityid", "cityName", "cityWeather")))
    }
}
package co.ruizhang.weatherapp.business

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface CityListRepository {
    fun getCities(useCache: Boolean): Single<List<CityModel>>
}

class CityListRepositoryImpl ( val weatherApi: WeatherApi): CityListRepository {
    override fun getCities(useCache: Boolean): Single<List<CityModel>> {
//        return Single.just(listOf(CityModel("cityid", "cityName", "cityWeather")))
        return weatherApi
            .weather(35.0,139.0, "en", getApiKey())
            .subscribeOn(Schedulers.io())
            .map { weather ->
                listOf(CityModel("a", "b", weather.forecast?.txtForecast?.forecastday?.first().toString()))
            }
    }

    private fun getApiKey() : String{
        return "eee56fea6143a11bbf946a0273cc5b81"
    }

}
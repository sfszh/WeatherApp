package co.ruizhang.weatherapp.business

import co.ruizhang.weatherapp.business.weather.CityWeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/group")
    @Headers("Content-type: application/json")
    fun weather(
        @Query("id") id: List<String>,
        @Query("units") units: String ,
        @Query("appid") appId: String
    ): Single<CityWeatherResponse>
}
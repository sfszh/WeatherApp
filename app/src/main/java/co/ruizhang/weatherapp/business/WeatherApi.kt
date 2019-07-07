package co.ruizhang.weatherapp.business

import co.ruizhang.weatherapp.business.weather.CityDetailResponse
import co.ruizhang.weatherapp.business.weather.CityListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/group")
    @Headers("Content-type: application/json")
    fun weather(
        @Query("id") id: List<Int>,
        @Query("units") units: String ,
        @Query("appid") appId: String
    ): Single<CityListResponse>

    @GET("/data/2.5/forecast")
    @Headers("Content-type: application/json")
    fun forcast(
        @Query("id") id: Int,
        @Query("cnt") cnt: Int,
        @Query("appid") appId: String
    ):Single<CityDetailResponse>
}
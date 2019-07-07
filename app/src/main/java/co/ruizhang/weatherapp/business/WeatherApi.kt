package co.ruizhang.weatherapp.business

import io.reactivex.Single
import org.koin.sampleapp.repository.data.weather.Weather
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/weather")
    @Headers("Content-type: application/json")
    fun weather(@Query("lat") lat: Double?,
                @Query("lon") lon: Double?,
                @Query("lang") lang: String,
                @Query("appid") appId: String): Single<Weather>
}
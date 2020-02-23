package co.ruizhang.weatherapp.business.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CityWeatherDao {
    @Query("SELECT * FROM CITY_WEATHERS")
    fun getAllWeather(): Single<List<CityWeatherDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: List<CityWeatherDBModel>): Completable

    @Query("DELETE FROM CITY_WEATHERS")
    fun deleteAllWeather()
}
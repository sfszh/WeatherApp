package co.ruizhang.weatherapp.business.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CityWeatherDBModel::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getCityWeatherDao(): CityWeatherDao
}
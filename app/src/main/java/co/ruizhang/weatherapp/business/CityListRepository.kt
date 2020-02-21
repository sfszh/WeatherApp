package co.ruizhang.weatherapp.business

import co.ruizhang.weatherapp.BuildConfig
import co.ruizhang.weatherapp.viewmodels.ResultData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

interface CityListRepository {
    val cities: Observable<ResultData<List<CityWeather>>>
    fun get(useCache: Boolean)
    fun addCity(city: SelectedCityModel)
}

class CityListRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val selectedCityStorage: SelectedCityStorage
) : CityListRepository {
    /**
     * @return event for refresh using cache or not
     */
    private val event: PublishSubject<OperationEvent> = PublishSubject.create()


    override val cities: Observable<ResultData<List<CityWeather>>> = event
        .subscribeOn(Schedulers.io())
        .flatMapSingle { event ->
            when (event) {
                is OperationEvent.Get -> selectedCityStorage.get()
                is OperationEvent.Add -> selectedCityStorage.add(event.city)
                is OperationEvent.Remove -> selectedCityStorage.remove(event.id)
            }
                .flatMap {
                    //todo get weather based on city information
                    Timber.d("api calling")
                    weatherApi
                        .weather(getCityIds().joinToString(","), "metrics", getApiKey())
                        .subscribeOn(Schedulers.io())
                        .map { weather -> weather.mapToDomain() }
                }

        }
        .map {
            ResultData.Success(it) as ResultData<List<CityWeather>>
        }
        .onErrorReturn {
            ResultData.Error(null, it)
        }

    override fun addCity(city: SelectedCityModel) {
        event.onNext(OperationEvent.Add(city))
    }


    override fun get(useCache: Boolean) {
        event.onNext(OperationEvent.Get(useCache))
    }

    private fun getApiKey(): String {
        return BuildConfig.WEATHER_API_KEY
    }

    //todo retrieve this from storage
    private fun getCityIds(): List<Int> {
        return listOf(524901, 703448, 2643743)
    }

    sealed class OperationEvent(refresh: Boolean) {
        class Add(val city: SelectedCityModel) : OperationEvent(false)
        class Get(refresh: Boolean) : OperationEvent(refresh)
        class Remove(val id: String) : OperationEvent(false)
    }

}
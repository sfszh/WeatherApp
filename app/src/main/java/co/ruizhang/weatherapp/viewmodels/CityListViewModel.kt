package co.ruizhang.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import co.ruizhang.weatherapp.business.CityListRepository
import co.ruizhang.weatherapp.business.CityWeather
import co.ruizhang.weatherapp.business.SelectedCityModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject


data class CityListViewData(
    val id: Int,
    val name: String,
    val weather: String
)


class CityListViewModel(private val repository: CityListRepository) : ViewModel() {
    val cityListResult: Observable<ViewResultData<List<CityListViewData>>>
        get() = repository
            .cities
            .subscribeOn(Schedulers.io())
            .map { listResult ->
                when (listResult) {
                    is ResultData.Error -> {
                        ViewResultData.Error(
                            listResult.data?.map { it.mapViewData() },
                            listResult.throwable
                        )
                    }
                    is ResultData.Success -> {
                        ViewResultData.Success(listResult.data?.map { it.mapViewData() })
                    }
                }
            }

    fun start() {
        repository.get(false)

    }

    fun refresh() {
        repository.get(true)
    }

    fun selectCity(city: SelectedCityModel) {
        repository.addCity(city)
    }


    private fun CityWeather.mapViewData(): CityListViewData {
        return CityListViewData(id, name, weather)
    }

}




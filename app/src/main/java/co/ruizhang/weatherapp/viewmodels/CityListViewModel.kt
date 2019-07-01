package co.ruizhang.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import co.ruizhang.weatherapp.business.CityListService
import co.ruizhang.weatherapp.business.CityModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject


data class CityListViewData(
    val name: String,
    val weather: String
)


class CityListViewModel(val service: CityListService) : ViewModel() {
    val cityListResult: Observable<ViewResultData<List<CityListViewData>>>
        get() = cityResultBS


    private val cityResultBS =
        BehaviorSubject.createDefault<ViewResultData<List<CityListViewData>>>(ViewResultData.Success(emptyList()))
    private val disposables: CompositeDisposable = CompositeDisposable()


    init {
        refresh()
    }

    fun refresh() {
        service.getCitys()
            .doOnSubscribe {
                cityResultBS.onNext(ViewResultData.Loading(cityResultBS.value.data))
            }
            .map { list ->
                list.map { it.mapViewData() }
            }
            .subscribeBy(
                onSuccess = {
                    cityResultBS.onNext(ViewResultData.Success(it))
                },
                onError = {
                    cityResultBS.onNext(ViewResultData.Error(cityResultBS.value.data, it))
                }
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun CityModel.mapViewData(): CityListViewData {
        return CityListViewData(name, weather)
    }

}


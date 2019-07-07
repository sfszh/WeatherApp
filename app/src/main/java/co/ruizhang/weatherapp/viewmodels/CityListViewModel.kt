package co.ruizhang.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import co.ruizhang.weatherapp.business.CityListRepository
import co.ruizhang.weatherapp.business.CityModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject


data class CityListViewData(
    val id: String,
    val name: String,
    val weather: String
)


class CityListViewModel(private val service: CityListRepository) : ViewModel() {
    val cityListResult: Observable<ViewResultData<List<CityListViewData>>>
        get() = cityResultBS


    private val cityResultBS =
        BehaviorSubject.createDefault<ViewResultData<List<CityListViewData>>>(ViewResultData.Success(emptyList()))
    private val disposables: CompositeDisposable = CompositeDisposable()


    init {
        load(useCache = true)
    }

    fun refresh() {
        load(useCache = false)
    }


    private fun load(useCache: Boolean) {
        service.getCities(useCache)
            .observeOn(AndroidSchedulers.mainThread())
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
        return CityListViewData(id, name, weather)
    }

}


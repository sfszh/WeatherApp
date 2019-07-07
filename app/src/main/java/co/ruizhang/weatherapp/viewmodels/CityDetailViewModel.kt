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

data class CityDetailViewData(
    val id: String,
    val name: String,
    val weather: String
)

class CityDetailViewModel(private val service: CityListRepository) : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    val cityDetailResult: Observable<ViewResultData<CityDetailViewData>>
        get() = cityResultBS

    private val cityResultBS =
        BehaviorSubject.createDefault<ViewResultData<CityDetailViewData>>(ViewResultData.Success(null))

    init {
        load(1, useCache = true)
    }

    fun refresh() {
        load(1, useCache = false)
    }

    private fun load(id: Int, useCache: Boolean) {
        service.get(id, useCache)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                cityResultBS.onNext(ViewResultData.Loading(cityResultBS.value.data))
            }
            .map { city ->
                city.mapViewData()
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

    private fun CityModel.mapViewData(): CityDetailViewData {
        return CityDetailViewData(id, name, weather)
    }
}
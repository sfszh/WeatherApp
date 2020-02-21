package co.ruizhang.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import co.ruizhang.weatherapp.business.CityDetailModel
import co.ruizhang.weatherapp.business.CityDetailRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject

data class CityDetailViewData(
    val id: Int,
    val name: String,
    val weathers: List<String>
)

class CityDetailViewModel(private val repository: CityDetailRepository) : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    private var idCache: Int? = null
    val cityDetailResult: Observable<ViewResultData<CityDetailViewData>>
        get() = cityResultBS

    private val cityResultBS =
        BehaviorSubject.createDefault<ViewResultData<CityDetailViewData>>(ViewResultData.Success(null))

    fun start(id: Int) {
        load(id, true)
        idCache = id
    }

    fun refresh() {
        idCache?.let { id ->
            load(id, useCache = false)
        }
        if(idCache == null) {
            cityResultBS.onNext(ViewResultData.Error(cityResultBS.value!!.data, IllegalStateException("can not find city")))
        }
    }

    private fun load(id: Int, useCache: Boolean) {
        repository.get(id, useCache)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                cityResultBS.onNext(ViewResultData.Loading(cityResultBS.value!!.data))
            }
            .map { city ->
                city.mapViewData()
            }
            .subscribeBy(
                onSuccess = {
                    cityResultBS.onNext(ViewResultData.Success(it))
                },
                onError = {
                    cityResultBS.onNext(ViewResultData.Error(cityResultBS.value!!.data, it))
                }
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun CityDetailModel.mapViewData(): CityDetailViewData {
        return CityDetailViewData(id, name, weathers)
    }
}
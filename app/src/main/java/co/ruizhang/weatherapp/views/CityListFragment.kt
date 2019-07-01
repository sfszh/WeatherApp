package co.ruizhang.weatherapp.views

import androidx.fragment.app.Fragment
import co.ruizhang.weatherapp.viewmodels.CityListViewModel
import co.ruizhang.weatherapp.viewmodels.ViewResultData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CityListFragment : Fragment() {
    private val cityListViewModel: CityListViewModel by viewModel()
    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        cityListViewModel.cityListResult
            .subscribeBy(
                onNext = { result ->
                    when (result) {
                        is ViewResultData.Loading -> {
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("loading empty")
                            } else {
                                Timber.d("loading with data")
                            }
                        }
                        is ViewResultData.Success -> {
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("success empty")
                            } else {
                                Timber.d("success with data")
                            }
                        }

                        is ViewResultData.Error -> {
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("Error empty")
                            } else {
                                Timber.d("Error with data")
                            }
                        }
                    }
                }
            )
            .addTo(
                disposable
            )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}
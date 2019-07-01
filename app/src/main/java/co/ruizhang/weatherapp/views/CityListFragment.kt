package co.ruizhang.weatherapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.ruizhang.weatherapp.R
import co.ruizhang.weatherapp.databinding.FragmentCityListBinding
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
    private lateinit var binding: FragmentCityListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        binding.swipeContainer.setOnRefreshListener {
            cityListViewModel.refresh()
        }
        cityListViewModel.cityListResult
            .subscribeBy(
                onNext = { result ->
                    when (result) {
                        is ViewResultData.Loading -> {
                            binding.emptyStateText.visibility = View.GONE
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("loading empty")
                                binding.emptyStateProgress.visibility = View.VISIBLE
                                binding.list.visibility = View.INVISIBLE
                            } else {
                                Timber.d("loading with data")
                                binding.emptyStateProgress.visibility = View.GONE
                                binding.list.visibility = View.VISIBLE
                            }
                        }
                        is ViewResultData.Success -> {
                            binding.emptyStateProgress.visibility = View.GONE
                            binding.swipeContainer.isRefreshing = false
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("success empty")
                                binding.emptyStateText.visibility = View.VISIBLE
                                binding.list.visibility = View.INVISIBLE
                            } else {
                                Timber.d("success with data")
                                binding.emptyStateText.visibility = View.GONE
                                binding.list.visibility = View.INVISIBLE
                            }
                        }

                        is ViewResultData.Error -> {
                            binding.emptyStateProgress.visibility = View.GONE
                            binding.swipeContainer.isRefreshing = false
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("Error empty")
                                binding.emptyStateText.visibility = View.VISIBLE
                                binding.list.visibility = View.INVISIBLE
                            } else {
                                Timber.d("Error with data ${result.throwable}")
                                binding.emptyStateText.visibility = View.GONE
                                binding.list.visibility = View.INVISIBLE
                                Toast.makeText(context, R.string.city_error_text, Toast.LENGTH_LONG).show()

                            }
                        }
                    }
                }
            )
            .addTo(disposable)
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}
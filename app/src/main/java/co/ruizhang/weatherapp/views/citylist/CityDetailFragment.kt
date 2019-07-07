package co.ruizhang.weatherapp.views.citylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import co.ruizhang.weatherapp.databinding.FragmentCityDetailBinding
import co.ruizhang.weatherapp.viewmodels.CityDetailViewModel
import co.ruizhang.weatherapp.viewmodels.ViewResultData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CityDetailFragment : Fragment() {

    private val cityDetailViewModel: CityDetailViewModel by viewModel()
    private var disposable: CompositeDisposable = CompositeDisposable()
    private lateinit var binding: FragmentCityDetailBinding

    val args: CityDetailFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCityDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        binding.swipeContainer.setOnRefreshListener {
            cityDetailViewModel.refresh()
        }
        cityDetailViewModel.start(args.cityId)

        cityDetailViewModel.cityDetailResult
            .subscribeBy(
                onNext = { result ->

                    if (result.data == null) {
                        binding.content.visibility = View.GONE
                    } else {
                        binding.content.visibility = View.VISIBLE
                        binding.title.text = result.data.name
                        binding.weather.text = result.data.weathers.joinToString()
                    }
                    when (result) {
                        is ViewResultData.Loading -> {
                            if (result.data == null) {
                                binding.emptyStateProgress.visibility = View.VISIBLE
                            }else {
                                binding.swipeContainer.isRefreshing = true
                            }
                        }
                        is ViewResultData.Success -> {
                            binding.swipeContainer.isRefreshing = false
                            binding.emptyStateProgress.visibility = View.GONE
                        }

                        is ViewResultData.Error -> {
                            Toast.makeText(context, result.throwable.localizedMessage, Toast.LENGTH_LONG).show()
                            binding.swipeContainer.isRefreshing = false
                            binding.emptyStateProgress.visibility = View.GONE
                        }
                    }
                },
                onError = {
                    Timber.d(it)
                })
            .addTo(disposable)
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }
}
package co.ruizhang.weatherapp.views.citylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.ruizhang.weatherapp.databinding.FragmentCityListBinding
import co.ruizhang.weatherapp.viewmodels.CityListViewData
import co.ruizhang.weatherapp.viewmodels.CityListViewModel
import co.ruizhang.weatherapp.viewmodels.ViewResultData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CityListFragment : Fragment(), CityListClickListener {
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

        binding.floatingActionButton.setOnClickListener {
            Toast.makeText(context, "To Be Implemented", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerView.adapter = CityListAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(CityListDecoration())
        cityListViewModel.cityListResult
            .subscribeBy(
                onNext = { result ->
                    when (result) {
                        is ViewResultData.Loading -> {
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("loading empty")
                                showEmptyState()
                            } else {
                                Timber.d("loading with data")
                                showLoadedState(result)
                            }
                            binding.emptyStateText.visibility = View.GONE

                        }
                        is ViewResultData.Success -> {
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("success empty")
                                showEmptyState()
                            } else {
                                Timber.d("success with data")
                                showLoadedState(result)
                            }
                            binding.emptyStateProgress.visibility = View.GONE
                            binding.swipeContainer.isRefreshing = false
                        }

                        is ViewResultData.Error -> {
                            if (result.data.isNullOrEmpty()) {
                                Timber.d("Error empty")
                                showEmptyState()
                            } else {
                                Timber.d("Error with data ${result.throwable}")
                                showLoadedState(result)
                            }
                            binding.emptyStateProgress.visibility = View.GONE
                            binding.swipeContainer.isRefreshing = false
                            Toast.makeText(context, result.throwable.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
            .addTo(disposable)
    }

    private fun showLoadedState(result: ViewResultData<List<CityListViewData>>) {
        binding.emptyStateText.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        (binding.recyclerView.adapter as CityListAdapter).submitList(result.data)
    }

    private fun showEmptyState() {
        binding.emptyStateText.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    override fun onCityClicked(city: CityListViewData) {
        Timber.d("city get clicked ${city.id}")
        val action = CityListFragmentDirections.actionCityListFragmentToCityDetailFragment(city.id)
//        val action = confirmationAction(amount)
        findNavController().navigate(action)

    }


}
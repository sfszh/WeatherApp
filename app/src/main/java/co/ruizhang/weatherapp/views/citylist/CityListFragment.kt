package co.ruizhang.weatherapp.views.citylist

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class CityListFragment : Fragment(), CityListClickListener {
    private val cityListViewModel: CityListViewModel by viewModel()
    private var disposable: CompositeDisposable = CompositeDisposable()
    private lateinit var binding: FragmentCityListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            openSearchActivity()
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
                            Toast.makeText(
                                context,
                                result.throwable.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
            .addTo(disposable)
    }

    private fun openSearchActivity() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME)

        // Start the autocomplete intent.
        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(context!!)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Timber.d("Place: ${place.name} , ${place.id}")
                }
                AutocompleteActivity.RESULT_ERROR -> { // TODO: Handle the error.
                    val status: Status = Autocomplete.getStatusFromIntent(data!!)
                    Timber.d(status.statusMessage)
                }
                RESULT_CANCELED -> { // The user canceled the operation.
                    Timber.d("canceled")
                }
            }
        }
    }

    companion object {
        const val AUTOCOMPLETE_REQUEST_CODE = 1
    }
}
package co.ruizhang.weatherapp.views.citysearch

import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import timber.log.Timber

class CitySearchFragment : AutocompleteSupportFragment() {

    override fun onStart() {
        super.onStart()
        this.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Specify the types of place data to return.
        this.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                Timber.d("place ${p0.address}")
            }

            override fun onError(p0: Status) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
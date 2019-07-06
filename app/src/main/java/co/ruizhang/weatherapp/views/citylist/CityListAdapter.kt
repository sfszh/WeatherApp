package co.ruizhang.weatherapp.views.citylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.ruizhang.weatherapp.databinding.CityListItemBinding
import co.ruizhang.weatherapp.viewmodels.CityListViewData


class CityListAdapter(private val listener: CityListClickListener) :
    ListAdapter<CityListViewData, CityListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CityListItemBinding.inflate(inflater, parent, false)
        return CityListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CityListViewData>() {
            override fun areItemsTheSame(oldItem: CityListViewData, newItem: CityListViewData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CityListViewData, newItem: CityListViewData): Boolean {
                return oldItem == newItem
            }
        }

    }
}


interface CityListClickListener {
    fun onCityClicked(city: CityListViewData)
}

class CityListViewHolder(
    private val binding: CityListItemBinding,
    private val listener: CityListClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CityListViewData) {
        binding.title.text = item.name
        binding.weather.text = item.weather
        binding.card.setOnClickListener {
            listener.onCityClicked(item)
        }
    }
}
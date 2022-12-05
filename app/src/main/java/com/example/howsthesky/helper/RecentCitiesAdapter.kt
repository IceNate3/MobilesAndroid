package com.example.howsthesky.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.howsthesky.databinding.ListItemRecentCitiesBinding

class RecentCitiesAdapter(val clickListener: RecentCitiesListener) :
    ListAdapter<Weather, RecentCitiesAdapter.ViewHolder>(RecentCitiesDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemRecentCitiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val res = itemView.context.resources

        fun bind(clickListener: RecentCitiesListener, item: Weather) {
            binding.cityWeather = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRecentCitiesBinding
                    .inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class RecentCitiesDiffCallback : DiffUtil.ItemCallback<Weather>() {
    override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
        return oldItem.cityName == newItem.cityName
    }

    override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
        return oldItem == newItem
    }
}

class RecentCitiesListener (val clickListener: (cityName: String) -> Unit) {
    fun onClick(city: Weather) = clickListener(city.cityName)
}
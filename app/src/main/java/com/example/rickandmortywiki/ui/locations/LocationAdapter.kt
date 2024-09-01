package com.example.rickandmortywiki.ui.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortywiki.data.entities.LocationEntity
import com.example.rickandmortywiki.databinding.LocationItemBinding
import com.example.rickandmortywiki.utils.diff.LocationDiffCallback
import com.example.rickandmortywiki.utils.extensions.residentIdList
import com.example.rickandmortywiki.utils.listeners.LocationItemClickListener

class LocationAdapter(private val listener: LocationItemClickListener) :
    ListAdapter<LocationEntity, LocationAdapter.ViewHolder>(LocationDiffCallback()) {
    inner class ViewHolder(private val binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationEntity) {
            binding.apply {
                val title =
                    "${location.name} ${if (location.dimension != "unknown") "(${location.dimension})" else ""}"
                titleTextView.text = title
                subTitleTextView.text = location.type
                root.setOnClickListener {
                    listener.onItemClick(location.residentIdList)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LocationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
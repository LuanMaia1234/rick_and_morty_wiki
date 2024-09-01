package com.example.rickandmortywiki.utils.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.rickandmortywiki.data.entities.LocationEntity

class LocationDiffCallback : DiffUtil.ItemCallback<LocationEntity>() {
    override fun areItemsTheSame(
        oldItem: LocationEntity,
        newItem: LocationEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: LocationEntity,
        newItem: LocationEntity
    ): Boolean {
        return oldItem == newItem
    }
}
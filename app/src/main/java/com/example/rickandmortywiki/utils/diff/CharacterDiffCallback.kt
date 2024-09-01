package com.example.rickandmortywiki.utils.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.rickandmortywiki.data.entities.CharacterEntity

class CharacterDiffCallback : DiffUtil.ItemCallback<CharacterEntity>() {
    override fun areItemsTheSame(
        oldItem: CharacterEntity,
        newItem: CharacterEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharacterEntity,
        newItem: CharacterEntity
    ): Boolean {
        return oldItem == newItem
    }
}
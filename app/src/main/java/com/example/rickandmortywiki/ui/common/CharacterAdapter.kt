package com.example.rickandmortywiki.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortywiki.R
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.databinding.CharacterItemBinding
import com.example.rickandmortywiki.utils.diff.CharacterDiffCallback
import com.example.rickandmortywiki.utils.extensions.drawable
import com.example.rickandmortywiki.utils.listeners.CharacterItemClickListener
import com.facebook.shimmer.Shimmer

class CharacterAdapter(private val listener: CharacterItemClickListener) :
    ListAdapter<CharacterEntity, CharacterAdapter.ViewHolder>(CharacterDiffCallback()) {
    inner class ViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterEntity) {
            binding.apply {
                Glide.with(imageView).load(character.image)
                    .placeholder(Shimmer.AlphaHighlightBuilder().drawable)
                    .error(R.drawable.ic_broken_image).into(imageView)
                nameTextView.text = character.name
                specieTextView.text = character.species
                root.setOnClickListener {
                    listener.onItemClick(character)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CharacterItemBinding.inflate(
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
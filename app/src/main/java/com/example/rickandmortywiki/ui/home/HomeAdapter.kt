package com.example.rickandmortywiki.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortywiki.R
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.databinding.CarouselItemBinding
import com.example.rickandmortywiki.utils.diff.CharacterDiffCallback
import com.example.rickandmortywiki.utils.extensions.drawable
import com.example.rickandmortywiki.utils.listeners.CharacterItemClickListener
import com.facebook.shimmer.Shimmer

class HomeAdapter(private val listener: CharacterItemClickListener) :
    ListAdapter<CharacterEntity, HomeAdapter.ViewHolder>(CharacterDiffCallback()) {
    inner class ViewHolder(private val binding: CarouselItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterEntity) {
            binding.apply {
                Glide.with(imageView).load(character.image)
                    .placeholder(Shimmer.AlphaHighlightBuilder().drawable)
                    .error(R.drawable.ic_broken_image).into(imageView)
                textView.text = character.name
                root.setOnClickListener {
                    listener.onItemClick(character)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CarouselItemBinding.inflate(
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
package com.example.rickandmortywiki.utils.listeners

import com.example.rickandmortywiki.data.entities.CharacterEntity

interface CharacterItemClickListener {
    fun onItemClick(character: CharacterEntity)
}
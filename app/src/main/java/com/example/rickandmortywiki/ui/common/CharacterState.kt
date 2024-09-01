package com.example.rickandmortywiki.ui.common

import com.example.rickandmortywiki.data.entities.CharacterEntity

sealed class CharacterState {
    data object Initial : CharacterState()
    data object Loading : CharacterState()
    data class Success(val characters: List<CharacterEntity>) : CharacterState()
    data class Error(val message: String) : CharacterState()
}
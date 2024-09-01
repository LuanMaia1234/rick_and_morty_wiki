package com.example.rickandmortywiki.ui.residents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepository
import com.example.rickandmortywiki.ui.common.CharacterState
import com.example.rickandmortywiki.utils.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResidentViewModel(private val repository: RickAndMortyRepository) : ViewModel()  {

    private val _state = MutableStateFlow<CharacterState>(CharacterState.Initial)
    val state: StateFlow<CharacterState> = _state

    fun getCharactersById(id: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        _state.value = CharacterState.Loading
        when (val result = repository.getCharactersById(id)) {
            is Resource.Success -> {
                _state.value = CharacterState.Success(result.data)
            }

            is Resource.Error -> {
                _state.value = CharacterState.Error(result.message)
            }
        }
    }
}
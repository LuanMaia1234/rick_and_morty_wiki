package com.example.rickandmortywiki.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortywiki.utils.resource.Resource
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepository
import com.example.rickandmortywiki.ui.common.CharacterState
import com.example.rickandmortywiki.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dispatcher: DispatcherProvider,
    private val repository: RickAndMortyRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CharacterState>(CharacterState.Initial)
    val state: StateFlow<CharacterState> = _state

    private var _currentPagePosition = 0
    val currentPagePosition get() = _currentPagePosition

    init {
        getCharacters()
    }

    fun getCharacters() = viewModelScope.launch(dispatcher.io) {
        _state.value = CharacterState.Loading
        when (val result = repository.getCharacters(page = 1, name = null)) {
            is Resource.Success -> {
                _state.value = CharacterState.Success(result.data.results)
            }

            is Resource.Error -> {
                _state.value = CharacterState.Error(result.message)
            }
        }
    }

    fun updatePagePosition(position: Int) {
        _currentPagePosition = position
    }
}
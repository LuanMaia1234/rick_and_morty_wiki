package com.example.rickandmortywiki.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.utils.resource.Resource
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: RickAndMortyRepository) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state

    private var search: String? = null

    init {
        getCharacters()
    }

    fun getCharacters(name: String? = null, isPagination: Boolean = false) =
        viewModelScope.launch(Dispatchers.IO) {
            if (search != name && !isPagination) {
                _state.value = SearchState.Initial
                search = name
            }
            val currentPage = _state.value.currentPage
            val totalPages = _state.value.totalPages
            val characters = _state.value.characters
            _state.value = SearchState.Loading(
                currentPage = currentPage,
                totalPages = totalPages,
                characters = characters
            )
            when (val result = repository.getCharacters(currentPage, search)) {
                is Resource.Success -> {
                    _state.value = SearchState.Success(
                        currentPage = currentPage + 1,
                        totalPages = result.data.info.pages,
                        characters = characters + result.data.results
                    )
                }

                is Resource.Error -> {
                    _state.value = SearchState.Error(
                        currentPage = currentPage,
                        totalPages = totalPages,
                        characters = characters,
                        message = result.message
                    )
                }
            }
        }

    sealed class SearchState(
        val currentPage: Int,
        val totalPages: Int,
        val characters: List<CharacterEntity>
    ) {
        data object Initial : SearchState(1, 0, emptyList())

        class Loading(
            currentPage: Int,
            totalPages: Int,
            characters: List<CharacterEntity>
        ) : SearchState(currentPage, totalPages, characters)

        class Success(
            currentPage: Int,
            totalPages: Int,
            characters: List<CharacterEntity>
        ) : SearchState(currentPage, totalPages, characters)

        class Error(
            currentPage: Int,
            totalPages: Int,
            characters: List<CharacterEntity>,
            val message: String
        ) : SearchState(currentPage, totalPages, characters)
    }
}
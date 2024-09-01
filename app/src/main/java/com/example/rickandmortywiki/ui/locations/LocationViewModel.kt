package com.example.rickandmortywiki.ui.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortywiki.utils.resource.Resource
import com.example.rickandmortywiki.data.entities.LocationEntity
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: RickAndMortyRepository) : ViewModel() {

    private val _state = MutableStateFlow<LocationState>(LocationState.Initial)
    val state: StateFlow<LocationState> = _state

    init {
        getLocations()
    }

    fun getLocations() = viewModelScope.launch(Dispatchers.IO) {
        val currentPage = _state.value.currentPage
        val totalPages = _state.value.totalPages
        val locations = _state.value.locations
        _state.value = LocationState.Loading(
            currentPage = currentPage,
            totalPages = totalPages,
            locations = locations
        )
        when (val result = repository.getLocations(currentPage)) {
            is Resource.Success -> {
                _state.value = LocationState.Success(
                    currentPage = currentPage + 1,
                    totalPages = result.data.info.pages,
                    locations = locations + result.data.results
                )
            }

            is Resource.Error -> {
                _state.value = LocationState.Error(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    locations = locations,
                    message = result.message
                )
            }
        }
    }

    sealed class LocationState(
        val currentPage: Int,
        val totalPages: Int,
        val locations: List<LocationEntity>
    ) {
        data object Initial : LocationState(1, 0, emptyList())

        class Loading(
            currentPage: Int,
            totalPages: Int,
            locations: List<LocationEntity>
        ) : LocationState(currentPage, totalPages, locations)

        class Success(
            currentPage: Int,
            totalPages: Int,
            locations: List<LocationEntity>
        ) : LocationState(currentPage, totalPages, locations)

        class Error(
            currentPage: Int,
            totalPages: Int,
            locations: List<LocationEntity>,
            val message: String
        ) : LocationState(currentPage, totalPages, locations)
    }
}
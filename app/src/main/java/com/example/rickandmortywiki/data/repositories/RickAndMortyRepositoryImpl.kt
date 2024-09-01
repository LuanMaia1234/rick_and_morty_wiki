package com.example.rickandmortywiki.data.repositories

import android.content.Context
import com.example.rickandmortywiki.utils.resource.Resource
import com.example.rickandmortywiki.data.entities.DataEntity
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.data.entities.LocationEntity
import com.example.rickandmortywiki.data.remote.ApiService

class RickAndMortyRepositoryImpl(private val apiService: ApiService, context: Context) :
    RickAndMortyRepository,
    BaseRepository(context) {
    override suspend fun getCharacters(
        page: Int,
        name: String?
    ): Resource<DataEntity<CharacterEntity>> {
        return handleApiCall { apiService.getCharacters(page, name) }
    }

    override suspend fun getCharactersById(id: List<String>): Resource<List<CharacterEntity>> {
        return handleApiCall { apiService.getCharactersById(id) }
    }

    override suspend fun getLocations(page: Int): Resource<DataEntity<LocationEntity>> {
        return handleApiCall { apiService.getLocations(page) }
    }
}
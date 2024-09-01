package com.example.rickandmortywiki.data.repositories

import com.example.rickandmortywiki.utils.resource.Resource
import com.example.rickandmortywiki.data.entities.DataEntity
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.data.entities.LocationEntity

interface RickAndMortyRepository {
    suspend fun getCharacters(page: Int, name: String?): Resource<DataEntity<CharacterEntity>>

    suspend fun getCharactersById(id: List<String>): Resource<List<CharacterEntity>>

    suspend fun getLocations(page: Int): Resource<DataEntity<LocationEntity>>
}
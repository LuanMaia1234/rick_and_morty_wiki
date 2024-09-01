package com.example.rickandmortywiki.data.remote

import com.example.rickandmortywiki.utils.constants.Constants
import com.example.rickandmortywiki.data.entities.DataEntity
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.data.entities.LocationEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(Constants.CHARACTER_END_POINT)
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?
    ): Response<DataEntity<CharacterEntity>>

    @GET("${Constants.CHARACTER_END_POINT}/{id}")
    suspend fun getCharactersById(@Path("id") id: List<String>): Response<List<CharacterEntity>>

    @GET(Constants.LOCATION_END_POINT)
    suspend fun getLocations(@Query("page") page: Int): Response<DataEntity<LocationEntity>>
}
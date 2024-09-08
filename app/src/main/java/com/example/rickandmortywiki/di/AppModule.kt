package com.example.rickandmortywiki.di

import com.example.rickandmortywiki.utils.constants.Constants
import com.example.rickandmortywiki.utils.enums.GenderEnum
import com.example.rickandmortywiki.utils.enums.StatusEnum
import com.example.rickandmortywiki.utils.enums.CustomEnumDeserializer
import com.example.rickandmortywiki.data.remote.ApiService
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepository
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepositoryImpl
import com.example.rickandmortywiki.ui.search.SearchViewModel
import com.example.rickandmortywiki.ui.home.HomeViewModel
import com.example.rickandmortywiki.ui.locations.LocationViewModel
import com.example.rickandmortywiki.ui.residents.ResidentViewModel
import com.example.rickandmortywiki.utils.dispatcher.DefaultDispatcherProvider
import com.example.rickandmortywiki.utils.dispatcher.DispatcherProvider
import com.google.gson.GsonBuilder
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                StatusEnum::class.java,
                CustomEnumDeserializer(StatusEnum::class.java, StatusEnum.UNKNOWN)
            )
            .registerTypeAdapter(
                GenderEnum::class.java,
                CustomEnumDeserializer(GenderEnum::class.java, GenderEnum.UNKNOWN)
            )
            .create()
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiService::class.java)
    }

    singleOf(::RickAndMortyRepositoryImpl) { bind<RickAndMortyRepository>() }
    singleOf(::DefaultDispatcherProvider) { bind<DispatcherProvider>() }
    viewModelOf(::HomeViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::LocationViewModel)
    viewModelOf(::ResidentViewModel)
}
package com.example.rickandmortywiki.ui.search

import app.cash.turbine.test
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.data.entities.DataEntity
import com.example.rickandmortywiki.data.entities.InfoEntity
import com.example.rickandmortywiki.data.entities.OriginEntity
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepository
import com.example.rickandmortywiki.utils.dispatcher.DispatcherProvider
import com.example.rickandmortywiki.utils.dispatcher.TestDispatcherProvider
import com.example.rickandmortywiki.utils.enums.GenderEnum
import com.example.rickandmortywiki.utils.enums.StatusEnum
import com.example.rickandmortywiki.utils.resource.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    private lateinit var dispatcher: DispatcherProvider
    private lateinit var repository: RickAndMortyRepository
    @Before
    fun setUp() {
        dispatcher = TestDispatcherProvider()
        repository = mockk<RickAndMortyRepository>()
    }

    private val data = DataEntity(
        InfoEntity(
            826,
            42,
            "https://rickandmortyapi.com/api/character/?page=2",
            null
        ),
        listOf(
            CharacterEntity(
                1,
                "Rick Sanchez",
                StatusEnum.ALIVE,
                "Human",
                "",
                GenderEnum.MALE,
                OriginEntity("Earth", "https://rickandmortyapi.com/api/location/1"),
                "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                emptyList(),
                "https://rickandmortyapi.com/api/character/1",
                "2017-11-04T18:48:46.250Z",
            )
        )
    )

    @Test
    fun `Should emit Success state when call getCharacters from repository`() = runTest {
        coEvery { repository.getCharacters(any(), any()) } returns Resource.Success(data)
        val viewModel = SearchViewModel(dispatcher, repository)
        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(SearchViewModel.SearchState.Success::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should emit Error state when call getCharacters from repository`() = runTest {
        coEvery { repository.getCharacters(any(), any()) } returns Resource.Error("Unexpected Error")
        val viewModel = SearchViewModel(dispatcher, repository)
        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(SearchViewModel.SearchState.Error::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
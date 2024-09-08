package com.example.rickandmortywiki.ui.residents

import app.cash.turbine.test
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.data.entities.OriginEntity
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepository
import com.example.rickandmortywiki.ui.common.CharacterState
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
class ResidentViewModelTest {
    private lateinit var dispatcher: DispatcherProvider
    private lateinit var repository: RickAndMortyRepository
    private lateinit var viewModel: ResidentViewModel

    @Before
    fun setUp() {
        dispatcher = TestDispatcherProvider()
        repository = mockk<RickAndMortyRepository>()
        viewModel = ResidentViewModel(dispatcher, repository)
    }

    private val characters = listOf(
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

    @Test
    fun `Should emit Success state when call getCharactersById from repository`() = runTest {
        coEvery { repository.getCharactersById(any()) } returns Resource.Success(characters)
        viewModel.getCharactersById(listOf("1"))
        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(CharacterState.Success::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should emit Error state when call getCharactersById from repository`() = runTest {
        coEvery { repository.getCharactersById(any()) } returns Resource.Error("Unexpected Error")
        viewModel.getCharactersById(listOf("1"))
        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(CharacterState.Error::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
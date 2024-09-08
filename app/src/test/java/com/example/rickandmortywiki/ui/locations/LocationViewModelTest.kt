package com.example.rickandmortywiki.ui.locations

import app.cash.turbine.test
import com.example.rickandmortywiki.data.entities.DataEntity
import com.example.rickandmortywiki.data.entities.InfoEntity
import com.example.rickandmortywiki.data.entities.LocationEntity
import com.example.rickandmortywiki.data.repositories.RickAndMortyRepository
import com.example.rickandmortywiki.utils.dispatcher.DispatcherProvider
import com.example.rickandmortywiki.utils.dispatcher.TestDispatcherProvider
import com.example.rickandmortywiki.utils.resource.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LocationViewModelTest {
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
            LocationEntity(
                1, "Earth",
                "Planet",
                "Dimension C-137",
                emptyList(),
                "https://rickandmortyapi.com/api/location/1",
                "2017-11-10T12:42:04.162Z"
            )
        )
    )

    @Test
    fun `Should emit Success state when call getLocations from repository`() = runTest {
        coEvery { repository.getLocations(any()) } returns Resource.Success(data)
        val viewModel = LocationViewModel(dispatcher, repository)
        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(LocationViewModel.LocationState.Success::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Should emit Error state when call getLocations from repository`() = runTest {
        coEvery { repository.getLocations(any()) } returns Resource.Error("Unexpected Error")
        val viewModel = LocationViewModel(dispatcher, repository)
        viewModel.state.test {
            assertThat(awaitItem()).isInstanceOf(LocationViewModel.LocationState.Error::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
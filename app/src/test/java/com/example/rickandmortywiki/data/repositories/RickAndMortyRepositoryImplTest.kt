package com.example.rickandmortywiki.data.repositories

import android.content.Context
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.data.entities.DataEntity
import com.example.rickandmortywiki.data.entities.InfoEntity
import com.example.rickandmortywiki.data.entities.LocationEntity
import com.example.rickandmortywiki.data.entities.OriginEntity
import com.example.rickandmortywiki.data.remote.ApiService
import com.example.rickandmortywiki.utils.enums.GenderEnum
import com.example.rickandmortywiki.utils.enums.StatusEnum
import com.example.rickandmortywiki.utils.resource.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RickAndMortyRepositoryImplTest {
    private lateinit var apiService: ApiService
    private lateinit var context: Context
    private lateinit var repository: RickAndMortyRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk<ApiService>()
        context = mockk<Context>()
        repository = RickAndMortyRepositoryImpl(apiService, context)
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

    private val locations = listOf(
        LocationEntity(
            1, "Earth",
            "Planet",
            "Dimension C-137",
            emptyList(),
            "https://rickandmortyapi.com/api/location/1",
            "2017-11-10T12:42:04.162Z"
        )
    )

    @Test
    fun `Should return Success when call getCharacters from apiService`() = runTest {
        coEvery { apiService.getCharacters(any(), any()) } returns Response.success(
            200,
            DataEntity(
                InfoEntity(
                    826,
                    42,
                    "https://rickandmortyapi.com/api/character/?page=2",
                    null
                ),
                characters
            )
        )
        val result = repository.getCharacters(1, null)
        assertThat(result).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun `Should return Error when call getCharacters from apiService`() = runTest {
        coEvery { apiService.getCharacters(any(), any()) } returns Response.error(
            400,
            ResponseBody.create(null, "Unexpected error")
        )
        val result = repository.getCharacters(1, null)
        assertThat(result).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `Should return Success when call getCharactersById from apiService`() = runTest {
        coEvery { apiService.getCharactersById(any()) } returns Response.success(200, characters)
        val result = repository.getCharactersById(listOf("1"))
        assertThat(result).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun `Should return Error when call getCharactersById from apiService`() = runTest {
        coEvery { apiService.getCharactersById(any()) } returns Response.error(
            400,
            ResponseBody.create(null, "Unexpected error")
        )
        val result = repository.getCharactersById(listOf("1"))
        assertThat(result).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `Should return Success when call getLocations from apiService`() = runTest {
        coEvery { apiService.getLocations(any()) } returns Response.success(
            200, DataEntity(
                InfoEntity(
                    126,
                    7,
                    "https://rickandmortyapi.com/api/location?page=2",
                    null
                ),
                locations
            )
        )
        val result = repository.getLocations(1)
        assertThat(result).isInstanceOf(Resource.Success::class.java)
    }

    @Test
    fun `Should return Error when call getLocations from apiService`() = runTest {
        coEvery { apiService.getLocations(any()) } returns Response.error(
            400,
            ResponseBody.create(null, "Unexpected error")
        )
        val result = repository.getLocations(1)
        assertThat(result).isInstanceOf(Resource.Error::class.java)
    }
}
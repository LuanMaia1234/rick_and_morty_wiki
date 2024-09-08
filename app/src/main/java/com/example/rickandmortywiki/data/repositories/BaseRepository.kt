package com.example.rickandmortywiki.data.repositories

import android.content.Context
import com.example.rickandmortywiki.R
import com.example.rickandmortywiki.utils.resource.Resource
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository(private val context: Context) {
    suspend fun <T> handleApiCall(execute: suspend () -> Response<T>): Resource<T> {
        return try {
            val result = execute()
            if (result.isSuccessful && result.body() != null) {
                Resource.Success(result.body()!!)
            } else {
                Resource.Error(
                    result.errorBody()?.string() ?: context.getString(R.string.unexpected_error)
                )
            }
        } catch (e: HttpException) {
            Resource.Error(e.message ?: context.getString(R.string.unexpected_error))
        } catch (e: IOException) {
            Resource.Error(context.getString(R.string.no_internet_error))
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.unexpected_error))
        }
    }
}
package com.example.rickandmortywiki.data.entities

import com.google.gson.annotations.SerializedName

data class DataEntity<T>(
    @SerializedName("info")
    val info: InfoEntity,
    @SerializedName("results")
    val results: List<T>
)
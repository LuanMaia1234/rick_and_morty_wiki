package com.example.rickandmortywiki.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OriginEntity(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) : Parcelable
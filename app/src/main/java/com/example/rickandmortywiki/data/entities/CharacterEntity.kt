package com.example.rickandmortywiki.data.entities

import android.os.Parcelable
import com.example.rickandmortywiki.utils.enums.GenderEnum
import com.example.rickandmortywiki.utils.enums.StatusEnum
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: StatusEnum,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("gender")
    val gender: GenderEnum,
    @SerializedName("origin")
    val origin: OriginEntity,
    @SerializedName("image")
    val image: String,
    @SerializedName("episode")
    val episode: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("created")
    val created: String
) : Parcelable
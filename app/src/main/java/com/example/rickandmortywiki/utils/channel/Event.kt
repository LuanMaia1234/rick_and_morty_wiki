package com.example.rickandmortywiki.utils.channel

sealed class Event {
    data class ShowSnackBar(val message: String) : Event()
}
package com.example.rickandmortywiki.utils.channel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object ChannelImpl {
    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    suspend fun send(event: Event) {
        eventChannel.send(event)
    }
}
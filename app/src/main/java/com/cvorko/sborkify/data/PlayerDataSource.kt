package com.cvorko.sborkify.data

import android.util.Log
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

interface PlayerController {

    fun playSongOfId(type: String)

    fun pause()

    fun resume()

    fun skipNext()

    fun skipPrevious()

    fun seekToPosition(position: Long)

    val playerStateFlow: Flow<PlayerState>

}

class PlayerControllerImpl(private val playerApi: PlayerApi) : PlayerController {

    override fun playSongOfId(type: String) {

        Log.d("test", type)
        playerApi.play(type)
    }

    override fun pause() {
        playerApi.pause()
    }

    override fun resume() {
        playerApi.resume()
    }

    override fun skipNext() {
        playerApi.skipNext()
    }

    override fun skipPrevious() {
        playerApi.skipPrevious()
    }

    override fun seekToPosition(position: Long) {
        playerApi.seekTo(position)
    }

    override val playerStateFlow: Flow<PlayerState> =
        playerApi
            .subscribeToPlayerState().toFlow()
            .shareIn(CoroutineScope(Dispatchers.Default), SharingStarted.WhileSubscribed(replayExpirationMillis = 0), replay = 1)

    private fun <T> Subscription<T>.toFlow() = callbackFlow {
        setEventCallback { trySend(it) }

        awaitClose {
            cancel()
        }
    }

}


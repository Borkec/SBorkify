package com.cvorko.sborkify.ui.viewmodel

import com.cvorko.sborkify.data.ContentRepository
import com.cvorko.sborkify.data.PlayerController
import com.cvorko.sborkify.ui.screen.CurrentTrackInfoViewState
import com.cvorko.sborkify.ui.screen.MainViewState
import com.cvorko.sborkify.ui.screen.PlayerControllerViewState
import com.cvorko.sborkify.ui.screen.PlayerViewState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class PlayerViewModel(
    private val contentRepository: ContentRepository,
    private val playerController: PlayerController
) : BaseViewModel<PlayerViewState>() {


    init {
        query {
            playerController.playerStateFlow.map {
                PlayerControllerViewState(
                    playbackPosition = it.playbackPosition,
                    isPaused = it.isPaused
                )
            }
        }

        query {
            playerController.playerStateFlow.mapNotNull { it.track }.map {
                CurrentTrackInfoViewState(
                    title = it.name,
                    artist = it.artist.name,
                    duration = it.duration,
                    trackImage = contentRepository.getImageForId(it.imageUri).first()
                )
            }
        }

    }

    fun seekToPosition(timestamp: Long) {
        playerController.seekToPosition(timestamp)
    }

    fun pauseCurrentSong() {
        playerController.pause()
    }

    fun resumeCurrentSong() {
        playerController.resume()
    }

    fun onSkipNext() {
        playerController.skipNext()
    }

    fun onSkipPrevious() {
        playerController.skipPrevious()
    }

}
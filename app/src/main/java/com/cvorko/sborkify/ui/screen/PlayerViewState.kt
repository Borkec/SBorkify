package com.cvorko.sborkify.ui.screen

import android.graphics.Bitmap

sealed class PlayerViewState : BaseViewState()

data class PlayerControllerViewState(val playbackPosition: Long, val isPaused: Boolean) : PlayerViewState() {
    companion object {
        val PREVIEW = PlayerControllerViewState(playbackPosition = 0, isPaused = false)
    }
}

data class CurrentTrackInfoViewState(val title: String, val artist: String, val duration: Long, val trackImage: Bitmap?): PlayerViewState() {
    companion object {
        val PREVIEW = CurrentTrackInfoViewState(title = "title", artist = "artist", 0, null)
    }
}
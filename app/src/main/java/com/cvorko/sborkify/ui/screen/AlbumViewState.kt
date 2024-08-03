package com.cvorko.sborkify.ui.screen

sealed class AlbumViewState : BaseViewState()

data class Content(
    val title: String,
    val artist: String,
    val duration: Long,
    val imageUrl: String,
    val tracks: List<TrackViewState>
) : AlbumViewState() {
    companion object {
        val PREVIEW = Content(title = "title", artist = "artist", 0, "", listOf())
    }
}

data class TrackViewState(val id: String, val title: String, val duration: String, val uri: String) {
    companion object {
        val PREVIEW = TrackViewState("id", "title", "0:0", "uri")
    }
}
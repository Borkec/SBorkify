package com.cvorko.sborkify.ui.viewmodel

import android.util.Log
import com.cvorko.sborkify.data.CallDataSource
import com.cvorko.sborkify.data.PlayerController
import com.cvorko.sborkify.ui.screen.AlbumViewState
import com.cvorko.sborkify.ui.screen.Content
import com.cvorko.sborkify.ui.screen.TrackViewState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

class AlbumViewModel(
    private val playlistId: String,
    private val callDataSource: CallDataSource,
    private val playerController: PlayerController
) : BaseViewModel<AlbumViewState>() {

    init {
        query {
            callDataSource
                .getAlbum(playlistId.split(":").last())
                .onEach { Log.d("AlbumViewModel", it.toString()) }
                .map { album ->
                    Content(
                        title = album.name,
                        artist = album.artists.map { it.name }.joinToString(),
                        duration = album.durationMs,
                        imageUrl = album.images.first().uri,
                        tracks = album.tracks.items.map {
                            TrackViewState(
                                id = it.id,
                                title = it.name,
                                duration = it.durationMs.milliseconds.toComponents { _, minutes, seconds, _ -> String.format("%02d:%02d", minutes, seconds)},
                                uri = it.uri
                            )
                        }
                    )
                }
        }

    }

    fun playSongItem(itemId: String) {
        playerController.playSongOfId(itemId)
    }
}
package com.cvorko.sborkify.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.cvorko.sborkify.ui.navigation.allDestinations
import com.cvorko.sborkify.ui.screen.CurrentTrackInfoViewState
import com.cvorko.sborkify.ui.screen.PlayerOverlay
import com.cvorko.sborkify.ui.screen.PlayerControllerViewState
import com.cvorko.sborkify.ui.theme.AppTheme
import com.cvorko.sborkify.ui.viewmodel.PlayerViewModel
import com.spotify.android.appremote.api.SpotifyAppRemote
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainOverlay()
        }
    }
}

@Composable
fun MainOverlay(
    playerViewModel: PlayerViewModel = koinViewModel<PlayerViewModel>()
) {
    AppTheme {
        val navController = rememberNavController()

        Surface(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            tonalElevation = 2.dp
        ) {

            val playerControllerViewState by playerViewModel.getViewState(PlayerControllerViewState.PREVIEW)
            val currentTrackInfoViewState by playerViewModel.getViewState(CurrentTrackInfoViewState.PREVIEW)

            Box {
                Column {
                    Text(
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp),
                        text = "SBorkify",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    NavHost(navController, navController.allDestinations())
                }

                PlayerOverlay(
                    playerControllerViewState = playerControllerViewState,
                    currentTrackInfo = currentTrackInfoViewState,
                    modifier = Modifier.align(Alignment.BottomCenter).zIndex(1f),
                    onPauseClicked = { playerViewModel.pauseCurrentSong() },
                    onResumeClicked = { playerViewModel.resumeCurrentSong() },
                    onSkipNextClicked = { playerViewModel.onSkipNext() },
                    onSkipPreviousClicked = { playerViewModel.onSkipPrevious() },
                    seekToPosition = { playerViewModel.seekToPosition(it) },
                )
            }
        }
    }
}
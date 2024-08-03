package com.cvorko.sborkify.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.createGraph
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cvorko.sborkify.ui.screen.MainActivityScreen
import com.cvorko.sborkify.ui.screen.AlbumListScreen
import com.cvorko.sborkify.ui.viewmodel.AlbumViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


sealed class Routes {

    @Serializable
    data object Start : Routes()

    @Serializable
    data class Playlist(val id: String) : Routes()
}

@Composable
fun NavController.allDestinations(): NavGraph {
    return remember(this) {
        createGraph(startDestination = Routes.Start) {
            composable<Routes.Start>() {
                MainActivityScreen ( onPlaylistClicked = { this@allDestinations.navigate(Routes.Playlist(it)) } )
            }
            composable<Routes.Playlist>() {
                val id = it.toRoute<Routes.Playlist>().id
                AlbumListScreen(
                    viewModel = koinViewModel<AlbumViewModel>(parameters = { parametersOf(id) })
                )
            }
        }
    }
}
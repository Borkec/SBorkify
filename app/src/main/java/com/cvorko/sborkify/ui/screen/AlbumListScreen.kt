package com.cvorko.sborkify.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cvorko.sborkify.ui.viewmodel.AlbumViewModel

@Composable
fun AlbumListScreen(
    viewModel: AlbumViewModel
) {

    val recommendedContentViewState by viewModel.getViewState(Content.PREVIEW)

    Column(modifier = Modifier.padding(16.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier) {
                Text(
                    text = recommendedContentViewState.title,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = recommendedContentViewState.artist,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            AsyncImage(
                alignment = Alignment.CenterEnd,
                modifier = Modifier.weight(1f).size(64.dp),
                model = recommendedContentViewState.imageUrl,
                contentDescription = null
            )
        }

        LazyColumn(modifier = Modifier.padding(top = 32.dp)) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Title",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Duration",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End
                    )
                }
                HorizontalDivider(Modifier.height(16.dp))
            }

            items(recommendedContentViewState.tracks, key = { it.id }) { track ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        viewModel.playSongItem(track.uri)
                    },
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = track.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = track.duration,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
            }
        }
    }

}
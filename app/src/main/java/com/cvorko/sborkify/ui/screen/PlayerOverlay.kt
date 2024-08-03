package com.cvorko.sborkify.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cvorko.sborkify.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun PlayerOverlay(
    playerControllerViewState: PlayerControllerViewState,
    currentTrackInfo: CurrentTrackInfoViewState,
    onPauseClicked: () -> Unit,
    onResumeClicked: () -> Unit,
    onSkipNextClicked: () -> Unit,
    onSkipPreviousClicked: () -> Unit,
    seekToPosition: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderState by remember(playerControllerViewState.playbackPosition) {
        mutableFloatStateOf(playerControllerViewState.playbackPosition.toFloat())
    }

    if (playerControllerViewState.isPaused.not()) {
        LaunchedEffect(sliderState) {
            while (true) {
                delay(1.seconds)
                sliderState += 1000
            }
        }
    }

    Column(
        modifier.fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(6.dp, RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
            ),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlaybackControls(
                isPlaybackPaused = playerControllerViewState.isPaused,
                modifier = Modifier.weight(1f),
                onPauseClicked = onPauseClicked,
                onResumeClicked = onResumeClicked,
                onSkipNextClicked = onSkipNextClicked,
                onSkipPreviousClicked = onSkipPreviousClicked
            )

            Slider(
                modifier = Modifier.weight(1f),
                value = sliderState,
                valueRange = 0f..currentTrackInfo.duration.toFloat(),
                onValueChange = {
                    sliderState = it
                },
                onValueChangeFinished = {
                    seekToPosition(sliderState.toLong())
                }
            )
        }

        currentTrackInfo.trackImage?.let { currentImage ->
            CurrentTrackInfo(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(start = 16.dp, end = 4.dp, bottom = 16.dp)
                    .height(80.dp),
                artist = currentTrackInfo.artist,
                title = currentTrackInfo.title,
                image = currentImage
            )
        }
    }

}

@Composable
fun PlaybackControls(
    isPlaybackPaused: Boolean,
    onPauseClicked: () -> Unit,
    onResumeClicked: () -> Unit,
    onSkipPreviousClicked: () -> Unit,
    onSkipNextClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Icon(
            modifier = Modifier.rotate(180f).size(20.dp)
                .clickable {
                    onSkipPreviousClicked()
                },
            painter = painterResource(R.drawable.skip_button),
            contentDescription = "skip next button"
        )

        Icon(
            modifier = Modifier.clip(CircleShape).size(40.dp)
                .clickable {
                    if (isPlaybackPaused)
                        onResumeClicked()
                    else
                        onPauseClicked()
                },
            painter = painterResource(if (isPlaybackPaused) R.drawable.play_button else R.drawable.pause_button),
            contentDescription = "play button"
        )


        Icon(
            modifier = Modifier.size(20.dp)
                .clickable {
                    onSkipNextClicked()
                },
            painter = painterResource(R.drawable.skip_button),
            contentDescription = "skip next button"
        )
    }

}

@Composable
fun CurrentTrackInfo(
    title: String,
    artist: String,
    image: Bitmap,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (true) {
            scrollState.scrollTo(0)
            delay(5.seconds)
            scrollState.animateScrollTo(scrollState.maxValue, animationSpec = TweenSpec(durationMillis = 10000, easing = LinearEasing))
            delay(5.seconds)
        }
    }
    Row(
        modifier = modifier.fillMaxSize().padding(start = 8.dp, top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier,
            bitmap = image.asImageBitmap(),
            contentDescription = "track image"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 16.dp)
                .verticalScroll(state = scrollState, enabled = false),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Text(
                text = artist,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}
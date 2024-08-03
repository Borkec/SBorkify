package com.cvorko.sborkify.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cvorko.sborkify.R
import com.cvorko.sborkify.ui.viewmodel.MainActivityViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainActivityScreen(
    viewModel: MainActivityViewModel = koinViewModel<MainActivityViewModel>(),
    onPlaylistClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val recommendedContentViewState by viewModel.getViewState(ContentViewState(mapOf()))

    LazyColumn(modifier) {
        items(recommendedContentViewState.contentList.keys.toList(), key = { it.id }) { contentItem ->
            val subList = recommendedContentViewState.contentList[contentItem]!!
            ContentList(
                contentType = contentItem.title,
                contentViewState = subList.contentList,
                subList = subList.child,
                onBoxClicked = { onPlaylistClicked(it) },
                onPlayClicked = { viewModel.playSongItem(it) }
            )
        }
    }

}

@Composable
fun ContentList(
    contentType: String,
    contentViewState: List<ContentItemViewState>,
    subList: OtherContentViewState?,
    onBoxClicked: (String) -> Unit,
    onPlayClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.animateContentSize()) {
        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 16.dp),
            text = contentType,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyRow {
            items(contentViewState, { it.id }) { item ->
                ContentItem(
                    contentItemViewState = item,
                    onBoxClicked = { id ->
                        onBoxClicked(id)
                    },
                    onPlayClicked = onPlayClicked
                )
            }
        }
        AnimatedVisibility(visible = subList != null) {
            if (subList != null) {
                LazyRow {
                    items(subList.contentList, { it.id }) {
                        ContentItem(
                            contentItemViewState = it,
                            onBoxClicked = {},
                            onPlayClicked = onPlayClicked
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun ContentItem(
    contentItemViewState: ContentItemViewState,
    onBoxClicked: (String) -> Unit,
    onPlayClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                onBoxClicked(contentItemViewState.id)
            }
            .shadow(6.dp, shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp), clip = false)
            .background(color = MaterialTheme.colorScheme.inverseSurface, shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
            .sizeIn(maxHeight = 280.dp, maxWidth = 160.dp, minHeight = 160.dp)
            .wrapContentHeight()
    ) {

        contentItemViewState.image?.let {
            Image(
                modifier = Modifier.heightIn(max = 160.dp).fillMaxWidth(),
                bitmap = contentItemViewState.image.asImageBitmap(),
                contentDescription = "Cover photo",
                contentScale = ContentScale.FillHeight
            )
        }

        Box(Modifier.padding(horizontal = 8.dp)) {
            Column {
                Text(
                    text = contentItemViewState.title,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )

                Text(
                    text = contentItemViewState.subtitle,
                    modifier = Modifier.padding(horizontal = 12.dp).padding(bottom = 24.dp),
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }

            Icon(
                modifier = Modifier.align(Alignment.CenterEnd).size(64.dp).alpha(0.25f).clickable { onPlayClicked(contentItemViewState.id) },
                painter = painterResource(R.drawable.play_button),
                tint = MaterialTheme.colorScheme.inverseOnSurface,
                contentDescription = "play content",
            )
        }

    }
}

@Preview
@Composable
fun RecommendedContentViewStatePreview(modifier: Modifier = Modifier) {
    ContentList(" ", listOf(), null, {}, {})
}

@Preview
@Composable
fun ContentItemPreview(modifier: Modifier = Modifier) {
    ContentItem(ContentItemViewState.PREVIEW, {}, {})
}



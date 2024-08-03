package com.cvorko.sborkify.ui.viewmodel

import android.util.Log
import com.cvorko.sborkify.data.ContentRepository
import com.cvorko.sborkify.data.PlayerController
import com.cvorko.sborkify.data.SongItem
import com.cvorko.sborkify.ui.screen.ContentItemViewState
import com.cvorko.sborkify.ui.screen.ContentViewState
import com.cvorko.sborkify.ui.screen.MainViewState
import com.cvorko.sborkify.ui.screen.OtherContentViewState
import com.spotify.android.appremote.api.ContentApi.ContentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class MainActivityViewModel(
    private val contentRepository: ContentRepository,
    private val playerController: PlayerController
) : BaseViewModel<MainViewState>() {

    private val songItemListPublisher = MutableStateFlow<Map<String, Flow<List<SongItem>?>>>(mapOf())

    private suspend fun getInitialContent(): Flow<List<ContentItemViewState>> =
        contentRepository.getRecommendedContentItems(ContentType.DEFAULT)
            .onStart { emit(emptyList()) }
            .map { map ->
                map.map {
                    ContentItemViewState(
                        id = it.id,
                        image = it.image,
                        title = it.title,
                        subtitle = it.subtitle,
                        playable = it.playable
                    )
                }
            }

    init {
        query {
            getInitialContent().onCompletion { Log.d("test", "quitting??? $it") }.onStart { Log.d("test", "start???") }
                .flatMapLatest { listContainerItem ->
                    combine(
                        listContainerItem.map { item ->
                            getListItemIdPublisherForContentType(item.id)
                                .map { m ->
                                    m?.map {
                                        ContentItemViewState(
                                            id = it.id,
                                            image = it.image,
                                            title = it.title,
                                            subtitle = it.subtitle,
                                            playable = it.playable
                                        )
                                    }
                                }
                        }
                    ) { recommendedItems ->
                        ContentViewState(
                            listContainerItem.zip(
                                recommendedItems.map {
                                    OtherContentViewState(it ?: listOf(), null)
                                }
                            ).toMap()
                        )
                    }
                }
        }

    }

    private fun getListItemIdPublisherForContentType(parentItemId: String) =
        songItemListPublisher.flatMapLatest {
            it.getOrDefault(parentItemId, contentRepository.getContentItemsForListItem(parentItemId))
        }

    fun playSongItem(itemId: String) {
        playerController.playSongOfId(itemId)
    }

}
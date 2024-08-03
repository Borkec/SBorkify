package com.cvorko.sborkify.data

import android.graphics.Bitmap
import android.util.Log
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

interface ContentRepository {

    suspend fun getRecommendedContentItems(type: String): Flow<List<SongItem>>

    suspend fun getContentItemsForListItem(id: String): Flow<List<SongItem>?>

    suspend fun getImageForId(imageUri: ImageUri): Flow<Bitmap>
}

class ContentRepositoryImpl(private val contentDataSource: ContentDataSource, private val imageDataSource: ImageDataSource) : ContentRepository {

    private val listItems: MutableStateFlow<List<ListItem>> = MutableStateFlow(emptyList())

    private val imageCache: MutableStateFlow<Map<ImageUri, Bitmap>> = MutableStateFlow(mapOf())

    override suspend fun getRecommendedContentItems(type: String) =
        contentDataSource
            .getRecommendedContentItems(type)
            .onEach { newItems -> listItems.update { it.plus(newItems.items) } }
            .mapLatest(::mapToSongItems)

    override suspend fun getContentItemsForListItem(id: String): Flow<List<SongItem>?> {
        val listItem = listItems.value.find { it.id == id }
        if (listItem == null) return flowOf()

        return contentDataSource
            .getChildOfItem(listItem, 3, 0)
            .mapLatest(::mapToSongItems)
    }

    override suspend fun getImageForId(imageUri: ImageUri): Flow<Bitmap> {
        return if (imageCache.value.containsKey(imageUri)) {
            flowOf(imageCache.value[imageUri]!!)
        } else {
            imageDataSource.getImage(imageUri).onEach { bitmap ->
                imageCache.update {
                    it.plus(imageUri to bitmap)
                }
            }
        }
    }

    private suspend fun mapToSongItems(listItems: ListItems) =
        listItems.items.map { item ->
            val bitmap = if (item.imageUri.raw.isNullOrEmpty()) null else imageDataSource.getImage(item.imageUri).first()
            SongItem(
                id = item.id,
                image = bitmap,
                title = item.title,
                subtitle = item.subtitle,
                streamUri = item.uri,
                playable = item.playable
            )
        }

}
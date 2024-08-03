package com.cvorko.sborkify.data

import com.cvorko.sborkify.model.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface CallDataSource {
    suspend fun getAlbum(id: String): Flow<Album>
}

class CallDataSourceImpl(private val spotifyAppService: SpotifyAppService) : BaseDataSource(), CallDataSource {

    override suspend fun getAlbum(id: String): Flow<Album> = flowOf(spotifyAppService.getAlbum(id))


}


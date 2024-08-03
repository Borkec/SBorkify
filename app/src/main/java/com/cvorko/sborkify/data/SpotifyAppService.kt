package com.cvorko.sborkify.data

import com.cvorko.sborkify.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface SpotifyAppService {

    @GET("albums/{id}")
    suspend fun getAlbum(@Path("id") id: String): Album
}


package com.cvorko.sborkify.model

import com.google.gson.annotations.SerializedName

data class Album(

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("album_type")
    val albumType: String,

    @SerializedName("total_tracks")
    val totalTracks: Int,

    @SerializedName("tracks")
    val tracks: Tracks,

    @SerializedName("artists")
    val artists: List<Artist>,

    @SerializedName("duration_ms")
    val durationMs: Long,

    @SerializedName("images")
    val images: List<ImageMetadata>,

    @SerializedName("uri")
    val uri: String
)

data class ImageMetadata(

    @SerializedName("url")
    val uri: String,

    @SerializedName("height")
    val height: Int,

    @SerializedName("width")
    val width: Int
)
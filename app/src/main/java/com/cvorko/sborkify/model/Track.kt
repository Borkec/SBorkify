package com.cvorko.sborkify.model

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("is_playable")
    val isPlayable: Boolean,

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("track_number")
    val trackNumber: Int,

    @SerializedName("duration_ms")
    val durationMs: Long,

    @SerializedName("uri")
    val uri: String
)
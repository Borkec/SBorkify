package com.cvorko.sborkify.model

import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("uri")
    val uri: String
)
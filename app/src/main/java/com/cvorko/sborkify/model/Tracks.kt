package com.cvorko.sborkify.model

import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("limit")
    val limit: Int,
    
    @SerializedName("offset")
    val offset: Int,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("items")
    val items: List<Track>
)
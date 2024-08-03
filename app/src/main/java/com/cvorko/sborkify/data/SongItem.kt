package com.cvorko.sborkify.data

import android.graphics.Bitmap

data class SongItem(
    val id: String,
    val image: Bitmap?,
    val playable: Boolean,
    val subtitle: String,
    val title: String,
    val streamUri: String
)

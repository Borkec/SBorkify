package com.cvorko.sborkify.ui.screen

import android.graphics.Bitmap

sealed class MainViewState : BaseViewState()

data class ContentViewState(
    val contentList: Map<ContentItemViewState, OtherContentViewState>,
) : MainViewState()

data class OtherContentViewState(
    val contentList: List<ContentItemViewState>,
    val child: OtherContentViewState?
) : MainViewState()

data class ContentItemViewState(
    val id: String,
    val image: Bitmap?,
    val title: String,
    val subtitle: String,
    val playable: Boolean
) : MainViewState() {
    companion object {
        val PREVIEW =
            ContentItemViewState(
                id = "000",
                image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
                title = "title",
                subtitle = "subtitle",
                playable = false
            )
    }
}
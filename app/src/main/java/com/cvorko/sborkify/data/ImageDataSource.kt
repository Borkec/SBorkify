package com.cvorko.sborkify.data

import android.graphics.Bitmap
import com.spotify.android.appremote.api.ImagesApi
import com.spotify.protocol.types.Image.Dimension
import com.spotify.protocol.types.ImageUri
import kotlinx.coroutines.flow.Flow

interface ImageDataSource {

    suspend fun getImage(imageUri: ImageUri): Flow<Bitmap>

    suspend fun getImage(imageUri: ImageUri, imageDimension: Dimension = Dimension.SMALL): Flow<Bitmap>
}

class ImageDataSourceImpl(private val imagesApi: ImagesApi) : BaseDataSource(), ImageDataSource {

    override suspend fun getImage(imageUri: ImageUri) = fetch {
        imagesApi.getImage(imageUri)
    }

    override suspend fun getImage(imageUri: ImageUri, imageDimension: Dimension) = fetch {
        imagesApi.getImage(imageUri, imageDimension)
    }
}


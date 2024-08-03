package com.cvorko.sborkify.data

import com.spotify.android.appremote.api.ContentApi
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import kotlinx.coroutines.flow.Flow

interface ContentDataSource {

    suspend fun getRecommendedContentItems(type: String): Flow<ListItems>

    suspend fun getChildOfItem(item: ListItem, perpage: Int, offset: Int): Flow<ListItems>

}

class ContentDataSourceImpl(private val contentApi: ContentApi) : BaseDataSource(), ContentDataSource {

    override suspend fun getRecommendedContentItems(type: String) = fetch {
        contentApi.getRecommendedContentItems(type)
    }

    override suspend fun getChildOfItem(item: ListItem, perpage: Int, offset: Int) = fetch {
        contentApi.getChildrenOfItem(item, perpage, offset)
    }

}


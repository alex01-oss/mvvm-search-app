package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.VideoId

interface YoutubeRepository {
    suspend fun getLatestVideoIds(channelId: String): List<VideoId>
}
package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.domain.model.youtube.Video

interface YoutubeRepository {
    suspend fun getLatestVideos(): List<Video>
}
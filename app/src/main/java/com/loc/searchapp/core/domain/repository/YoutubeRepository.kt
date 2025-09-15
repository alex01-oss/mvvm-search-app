package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.PlaylistItem

interface YoutubeRepository {
    suspend fun getLatestVideos(): List<PlaylistItem>
}
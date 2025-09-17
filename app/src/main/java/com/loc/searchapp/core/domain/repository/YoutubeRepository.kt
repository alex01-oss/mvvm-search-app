package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.PlaylistItem
import retrofit2.Response

interface YoutubeRepository {
    suspend fun getLatestVideos(): Response<List<PlaylistItem>>
}
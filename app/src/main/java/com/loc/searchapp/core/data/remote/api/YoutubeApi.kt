package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.YouTubeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("playlistItems")
    suspend fun getPlaylistVideos(
        @Query("part") part: String = "snippet",
        @Query("playlistId") playlistId: String,
        @Query("maxResults") maxResults: Int = 3,
        @Query("key") apiKey: String
    ): YouTubeResponse
}
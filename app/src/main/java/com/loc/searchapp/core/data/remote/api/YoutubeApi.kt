package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.YouTubeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("search")
    suspend fun getLatestVideos(
        @Query("key") apiKey: String,
        @Query("channelId") channelId: String,
        @Query("part") part: String = "snippet,id",
        @Query("order") order: String = "date",
        @Query("maxResults") maxResults: Int = 3
    ): YouTubeResponse
}
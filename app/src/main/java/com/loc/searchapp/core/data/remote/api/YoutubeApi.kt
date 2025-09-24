package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.YoutubeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface YoutubeApi {
    @GET("playlistItems")
    suspend fun getPlaylistVideos(
        @QueryMap params: Map<String, String>
    ): Response<YoutubeResponse>
}
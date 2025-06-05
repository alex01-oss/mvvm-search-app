package com.loc.searchapp.core.data.repository

import android.util.Log
import com.loc.searchapp.core.data.remote.api.YoutubeApi
import com.loc.searchapp.core.data.remote.dto.VideoId
import com.loc.searchapp.core.domain.repository.YoutubeRepository

class YoutubeRepositoryImpl(
    private val api: YoutubeApi,
    private val apiKey: String
) : YoutubeRepository {
    override suspend fun getLatestVideoIds(channelId: String): List<VideoId> {
        return try {
            val response = api.getLatestVideos(apiKey, channelId)
            response.items
                .map { it.id }
                .filter { it.kind == "youtube#video" && !it.videoId.isNullOrBlank() }
                .map { VideoId(kind = it.kind, videoId = it.videoId!!) }
        } catch (e: Exception) {
            Log.e("YouTube", "API error: ${e.localizedMessage}", e)
            emptyList()
        }
    }
}
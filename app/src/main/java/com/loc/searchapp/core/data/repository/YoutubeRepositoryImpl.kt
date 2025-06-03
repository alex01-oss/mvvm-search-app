package com.loc.searchapp.core.data.repository

import android.util.Log
import com.loc.searchapp.core.data.remote.api.YoutubeApi
import com.loc.searchapp.core.domain.repository.YoutubeRepository

class YoutubeRepositoryImpl(
    private val api: YoutubeApi,
    private val apiKey: String
) : YoutubeRepository {
    override suspend fun getLatestVideoIds(channelId: String): List<String> {
        return try {
            val response = api.getLatestVideos(apiKey, channelId)
            response.items
                .filter { it.id.kind == "youtube#video" }
                .mapNotNull { it.id.videoId }
        } catch (e: Exception) {
            Log.e("YouTube", "API error: ${e.localizedMessage}", e)
            emptyList()
        }
    }

}
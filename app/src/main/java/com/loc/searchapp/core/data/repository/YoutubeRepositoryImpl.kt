package com.loc.searchapp.core.data.repository

import android.util.Log
import com.loc.searchapp.core.data.remote.api.YoutubeApi
import com.loc.searchapp.core.data.remote.dto.PlaylistItem
import com.loc.searchapp.core.domain.repository.YoutubeRepository

class YoutubeRepositoryImpl(
    private val api: YoutubeApi,
    private val apiKey: String,
    private val playlistId: String
) : YoutubeRepository {

    override suspend fun getLatestVideos(): List<PlaylistItem> {
        return try {
            val response = api.getPlaylistVideos(
                playlistId = playlistId,
                apiKey = apiKey
            )
            response.items.filter {
                it.snippet.resourceId.videoId?.isNotBlank() ?: false
            }
        } catch (e: Exception) {
            Log.e("YouTube", "API error: ${e.localizedMessage}", e)
            emptyList()
        }
    }
}
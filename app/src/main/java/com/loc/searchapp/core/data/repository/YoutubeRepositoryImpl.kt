package com.loc.searchapp.core.data.repository

import android.util.Log
import com.loc.searchapp.core.data.remote.api.YoutubeApi
import com.loc.searchapp.core.data.remote.dto.PlaylistItem
import com.loc.searchapp.core.domain.repository.YoutubeRepository
import retrofit2.Response

class YoutubeRepositoryImpl(
    private val api: YoutubeApi,
    private val apiKey: String,
    private val playlistId: String
) : YoutubeRepository {

    override suspend fun getLatestVideos(): Response<List<PlaylistItem>> {
        return try {
            val response = api.getPlaylistVideos(
                playlistId = playlistId,
                apiKey = apiKey
            )

            if (response.isSuccessful) {
                val body = response.body()
                val items = body?.items?.filter {
                    it.snippet.resourceId.videoId?.isNotBlank() == true
                } ?: emptyList()

                Response.success(items)
            } else {
                Response.error(response.code(), response.errorBody()!!)
            }

        } catch (e: Exception) {
            Log.e("YouTube", "API error: ${e.localizedMessage}", e)
            Response.success(emptyList())
        }
    }
}
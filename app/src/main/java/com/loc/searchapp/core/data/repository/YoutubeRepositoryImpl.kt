package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.mappers.toDomain
import com.loc.searchapp.core.data.mappers.toQueryMap
import com.loc.searchapp.core.data.remote.api.YoutubeApi
import com.loc.searchapp.core.domain.model.youtube.Video
import com.loc.searchapp.core.domain.model.youtube.YoutubeData
import com.loc.searchapp.core.domain.repository.YoutubeRepository
import jakarta.inject.Inject

class YoutubeRepositoryImpl @Inject constructor(
    private val api: YoutubeApi,
    private val data: YoutubeData
) : YoutubeRepository {

    override suspend fun getLatestVideos(): List<Video> {
        return try {
            val response = api.getPlaylistVideos(data.toQueryMap())

            if (response.isSuccessful) {
                response.body()?.items?.toDomain() ?: emptyList()
            } else {
                throw RuntimeException("YouTube API error: ${response.code()} ${response.message()}")
            }

        } catch (e: Exception) {
            throw RuntimeException("Failed to fetch latest videos: ${e.localizedMessage}", e)
        }
    }
}
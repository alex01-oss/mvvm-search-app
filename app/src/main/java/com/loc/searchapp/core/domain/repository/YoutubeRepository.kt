package com.loc.searchapp.core.domain.repository

interface YoutubeRepository {
    suspend fun getLatestVideoIds(channelId: String): List<String>
}
package com.loc.searchapp.core.domain.usecases.youtube

import com.loc.searchapp.core.data.remote.dto.VideoId
import com.loc.searchapp.core.domain.repository.YoutubeRepository
import javax.inject.Inject

class GetLatestVideos @Inject constructor(
    private val repository: YoutubeRepository
) {
    suspend operator fun invoke(channelId: String): List<VideoId> {
        return repository.getLatestVideoIds(channelId)
    }
}
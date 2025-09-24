package com.loc.searchapp.core.domain.usecases.youtube

import com.loc.searchapp.core.domain.model.youtube.Video
import com.loc.searchapp.core.domain.repository.YoutubeRepository
import jakarta.inject.Inject

class GetLatestVideos @Inject constructor(
    private val repository: YoutubeRepository
) {
    suspend operator fun invoke(): List<Video> {
        return repository.getLatestVideos()
    }
}
package com.loc.searchapp.core.domain.usecases.youtube

import com.loc.searchapp.core.data.remote.dto.PlaylistItem
import com.loc.searchapp.core.domain.repository.YoutubeRepository
import javax.inject.Inject

class GetLatestVideos @Inject constructor(
    private val repository: YoutubeRepository
) {
    suspend operator fun invoke(): List<PlaylistItem> {
        return repository.getLatestVideos()
    }
}
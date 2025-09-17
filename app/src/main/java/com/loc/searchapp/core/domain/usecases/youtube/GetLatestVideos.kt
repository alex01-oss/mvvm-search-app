package com.loc.searchapp.core.domain.usecases.youtube

import com.loc.searchapp.core.data.remote.dto.PlaylistItem
import com.loc.searchapp.core.domain.repository.YoutubeRepository
import retrofit2.Response
import javax.inject.Inject

class GetLatestVideos @Inject constructor(
    private val repository: YoutubeRepository
) {
    suspend operator fun invoke(): Response<List<PlaylistItem>> {
        return repository.getLatestVideos()
    }
}
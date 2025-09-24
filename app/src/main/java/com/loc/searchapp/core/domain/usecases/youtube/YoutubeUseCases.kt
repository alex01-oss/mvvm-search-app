package com.loc.searchapp.core.domain.usecases.youtube

import jakarta.inject.Inject

data class YoutubeUseCases @Inject constructor(
    val getLatestVideos: GetLatestVideos
)
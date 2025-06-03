package com.loc.searchapp.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class YouTubeResponse(
    val items: List<YouTubeVideoItem>
)

@Serializable
data class YouTubeVideoItem(
    val id: VideoId
)

@Serializable
data class VideoId(
    val kind: String,
    val videoId: String? = null
)


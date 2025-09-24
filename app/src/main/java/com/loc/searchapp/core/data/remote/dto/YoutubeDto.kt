package com.loc.searchapp.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class YoutubeResponse(
    val items: List<PlaylistItemDto>
)
@Serializable
data class PlaylistItemDto(
    val snippet: SnippetDto
)

@Serializable
data class SnippetDto(
    val title: String,
    val thumbnails: ThumbnailsDto,
    val resourceId: ResourceIdDto
)

@Serializable
data class ThumbnailsDto(
    val high: ThumbnailDto? = null,
    val medium: ThumbnailDto? = null,
)

@Serializable
data class ThumbnailDto(
    val url: String
)

@Serializable
data class ResourceIdDto(
    val videoId: String? = null
)
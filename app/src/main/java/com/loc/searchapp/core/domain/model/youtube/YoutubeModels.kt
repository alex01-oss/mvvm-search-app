package com.loc.searchapp.core.domain.model.youtube

data class YoutubeData(
    val part: String = "snippet",
    val playlistId: String,
    val maxResults: Int = 3,
    val apiKey: String
)

data class Video(
    val snippet: Snippet
)

data class Snippet(
    val title: String,
    val thumbnails: Thumbnails,
    val resourceId: ResourceId
)

data class Thumbnails(
    val high: Thumbnail?,
    val medium: Thumbnail?,
)

data class Thumbnail(
    val url: String
)

data class ResourceId(
    val videoId: String?
)
package com.loc.searchapp.core.data.mappers

import com.loc.searchapp.core.data.remote.dto.PlaylistItemDto
import com.loc.searchapp.core.data.remote.dto.ThumbnailDto
import com.loc.searchapp.core.domain.model.youtube.ResourceId
import com.loc.searchapp.core.domain.model.youtube.Snippet
import com.loc.searchapp.core.domain.model.youtube.Thumbnail
import com.loc.searchapp.core.domain.model.youtube.Thumbnails
import com.loc.searchapp.core.domain.model.youtube.Video
import com.loc.searchapp.core.domain.model.youtube.YoutubeData

fun YoutubeData.toQueryMap(): Map<String, String> {
    return mapOf(
        "part" to part,
        "playlistId" to playlistId,
        "maxResults" to maxResults.toString(),
        "key" to apiKey
    )
}

fun List<PlaylistItemDto>.toDomain(): List<Video> {
    return this.mapNotNull { it.toDomain() }
}

fun PlaylistItemDto.toDomain(): Video? {
    val id = snippet.resourceId.videoId ?: return null

    return Video(
        snippet = Snippet(
            title = snippet.title,
            thumbnails = Thumbnails(
                high = snippet.thumbnails.high?.toDomain(),
                medium = snippet.thumbnails.medium?.toDomain()
            ),
            resourceId = ResourceId(id)
        )
    )
}

fun ThumbnailDto.toDomain() = Thumbnail(url = url)

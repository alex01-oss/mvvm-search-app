package com.loc.searchapp.presentation.shared.mapper

import com.loc.searchapp.core.domain.model.youtube.Video
import com.loc.searchapp.presentation.shared.model.VideoUi

fun Video.toUi(): VideoUi? {
    val id = snippet.resourceId.videoId ?: return null
    val thumbnail = snippet.thumbnails.high?.url
        ?: snippet.thumbnails.medium?.url
        ?: return null

    return VideoUi(
        videoId = id,
        title = snippet.title,
        thumbnailUrl = thumbnail
    )
}

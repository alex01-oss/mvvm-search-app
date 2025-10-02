package com.loc.searchapp.core.data.mappers

import com.loc.searchapp.core.data.remote.dto.DeletePostResponse
import com.loc.searchapp.core.data.remote.dto.ImageUploadResponse
import com.loc.searchapp.core.data.remote.dto.PostRequest
import com.loc.searchapp.core.data.remote.dto.PostResponse
import com.loc.searchapp.core.domain.model.catalog.Limit
import com.loc.searchapp.core.domain.model.posts.DeletePostResult
import com.loc.searchapp.core.domain.model.posts.EditPostData
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.model.posts.PostData
import com.loc.searchapp.core.domain.model.posts.PostId
import com.loc.searchapp.core.domain.model.posts.UploadData
import com.loc.searchapp.core.domain.model.posts.UploadResult
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

fun PostResponse.toDomain(): Post {
    return Post(
        id = this.id,
        title = this.title,
        content = this.content,
        image = this.image,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        userId = this.userId
    )
}

fun PostData.toDto(): PostRequest {
    return PostRequest(
        title = this.title,
        content = this.content,
        image = this.image
    )
}

fun PostId.toDto(): Int = this.postId

fun DeletePostResponse.toDomain(): DeletePostResult {
    return DeletePostResult(
        detail = this.detail
    )
}

fun List<PostResponse>.toDomain(): List<Post> {
    return this.map { it.toDomain() }
}

fun UploadData.toDto(): MultipartBody.Part {
    val mimeType = when (file.extension.lowercase()) {
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        "webp" -> "image/webp"
        else -> "application/octet-stream"
    }

    return MultipartBody.Part.createFormData(
        name = "file",
        filename = file.name,
        body = file.asRequestBody(mimeType.toMediaType())
    )
}

fun ImageUploadResponse.toDomain(): UploadResult {
    return UploadResult(
        filename = this.filename,
        url = this.url
    )
}

fun EditPostData.toDto(): Pair<Int, PostRequest> {
    return id to PostRequest(
        title = title,
        content = content,
        image = image
    )
}

fun Limit.toQueryParam(): Int = this.limit
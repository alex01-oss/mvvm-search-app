package com.loc.searchapp.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val title: String,
    val content: String,
    val image: String? = null
)

@Serializable
data class PostResponse(
    val id: Int,
    val title: String,
    val content: String,
    val image: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("user_id") val userId: Int
)

@Serializable
data class EditPostRequest(
    val title: String,
    val content: String,
    val image: String? = null
)

@Serializable
data class DeletePostResponse(
    val detail: String
)

@Serializable
data class ImageUploadResponse(
    val filename: String,
    val url: String
)

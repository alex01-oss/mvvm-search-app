package com.loc.searchapp.core.domain.model.posts

import java.io.File

data class UploadData(
    val file: File
)

data class UploadResult(
    val filename: String,
    val url: String
)

data class PostId(
    val postId: Int
)

data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val image: String?,
    val createdAt: String,
    val updatedAt: String,
    val userId: Int
)

data class PostData(
    val title: String,
    val content: String,
    val image: String?
)

data class EditPostData(
    val id: Int,
    val title: String,
    val content: String,
    val image: String?
)

data class DeletePostResult(
    val detail: String
)
package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.mappers.toDomain
import com.loc.searchapp.core.data.mappers.toDto
import com.loc.searchapp.core.data.mappers.toQueryParam
import com.loc.searchapp.core.data.remote.api.PostsApi
import com.loc.searchapp.core.domain.model.catalog.Limit
import com.loc.searchapp.core.domain.model.posts.DeletePostResult
import com.loc.searchapp.core.domain.model.posts.EditPostData
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.model.posts.PostData
import com.loc.searchapp.core.domain.model.posts.PostId
import com.loc.searchapp.core.domain.model.posts.UploadData
import com.loc.searchapp.core.domain.model.posts.UploadResult
import com.loc.searchapp.core.domain.repository.PostsRepository
import jakarta.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val api: PostsApi
) : PostsRepository {

    override suspend fun createPost(data: PostData): Post {
        val res = api.createPost(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Post creation successful but body is null")
        } else {
            throw RuntimeException("Post creation failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun deletePost(data: PostId): DeletePostResult {
        val res = api.deletePost(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Delete post successful but body is null")
        } else {
            throw RuntimeException("Delete post failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun editPost(data: EditPostData): Post {
        val res = api.editPost(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Edit post successful but body is null")
        } else {
            throw RuntimeException("Edit post failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun getAllPosts(data: Limit?): List<Post> {
        val res = api.getAllPosts(data?.toQueryParam())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Get all posts successful but body is null")
        } else {
            throw RuntimeException("Get all posts failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun getPostById(data: PostId): Post {
        val res = api.getPostById(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Get post by id successful but body is null")
        } else {
            throw RuntimeException("Get post by id failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun uploadImage(data: UploadData): UploadResult {
        val res = api.uploadImage(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Image upload successful but body is null")
        } else {
            throw RuntimeException("Image upload failed: ${res.code()} ${res.message()}")
        }
    }
}
package com.loc.searchapp.domain.repository

import com.loc.searchapp.data.remote.dto.CreatePostRequest
import com.loc.searchapp.data.remote.dto.DeletePostResponse
import com.loc.searchapp.data.remote.dto.EditPostRequest
import com.loc.searchapp.data.remote.dto.ImageUploadResponse
import com.loc.searchapp.data.remote.dto.PostResponse
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File

interface PostsRepository {
    suspend fun createPost(
        request: CreatePostRequest
    ): Response<PostResponse>

    suspend fun deletePost(
        postId: Int
    ): Response<DeletePostResponse>

    suspend fun editPost(
        postId: Int,
        request: EditPostRequest
    ): Response<PostResponse>

    suspend fun getAllPosts(): Response<List<PostResponse>>

    suspend fun getPostById(
        postId: Int
    ): Response<PostResponse>

    suspend fun uploadImage(
        file: MultipartBody.Part
    ): Response<ImageUploadResponse>
}
package com.loc.searchapp.data.repository

import com.loc.searchapp.data.remote.api.PostsApi
import com.loc.searchapp.data.remote.dto.CreatePostRequest
import com.loc.searchapp.data.remote.dto.DeletePostResponse
import com.loc.searchapp.data.remote.dto.EditPostRequest
import com.loc.searchapp.data.remote.dto.ImageUploadResponse
import com.loc.searchapp.data.remote.dto.PostResponse
import com.loc.searchapp.domain.repository.PostsRepository
import okhttp3.MultipartBody
import retrofit2.Response

class PostsRepositoryImpl(
    private val postsApi: PostsApi
) : PostsRepository {
    override suspend fun createPost(
        request: CreatePostRequest
    ): Response<PostResponse> {
        return postsApi.createPost(request)
    }

    override suspend fun deletePost(
        postId: Int
    ): Response<DeletePostResponse> {
        return postsApi.deletePost(postId)
    }

    override suspend fun editPost(
        postId: Int,
        request: EditPostRequest
    ): Response<PostResponse> {
        return postsApi.editPost(postId, request)
    }

    override suspend fun getAllPosts(): Response<List<PostResponse>> {
        return postsApi.getAllPosts()
    }

    override suspend fun getPostById(
        postId: Int
    ): Response<PostResponse> {
        return postsApi.getPostById(postId)
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part
    ): Response<ImageUploadResponse> {
        return postsApi.uploadImage(file)
    }
}
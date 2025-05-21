package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.CreatePostRequest
import com.loc.searchapp.core.data.remote.dto.DeletePostResponse
import com.loc.searchapp.core.data.remote.dto.EditPostRequest
import com.loc.searchapp.core.data.remote.dto.ImageUploadResponse
import com.loc.searchapp.core.data.remote.dto.PostResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PostsApi {
    @POST("/api/blog")
    suspend fun createPost(
        @Body request: CreatePostRequest
    ): Response<PostResponse>

    @DELETE("/api/blog/{post_id}")
    suspend fun deletePost(
        @Path("post_id") postId: Int
    ): Response<DeletePostResponse>

    @PUT("/api/blog/{post_id}")
    suspend fun editPost(
        @Path("post_id") postId: Int,
        @Body request: EditPostRequest
    ): Response<PostResponse>

    @GET("/api/blog")
    suspend fun getAllPosts(): Response<List<PostResponse>>

    @GET("/api/blog/{post_id}")
    suspend fun getPostById(
        @Path("post_id") postId: Int?
    ): Response<PostResponse>

    @Multipart
    @POST("/api/upload-image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<ImageUploadResponse>
}
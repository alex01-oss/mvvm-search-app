package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.DeletePostResponse
import com.loc.searchapp.core.data.remote.dto.ImageUploadResponse
import com.loc.searchapp.core.data.remote.dto.PostRequest
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
import retrofit2.http.Query

interface PostsApi {
    @POST("blog")
    suspend fun createPost(
        @Body request: PostRequest
    ): Response<PostResponse>

    @DELETE("blog/{post_id}")
    suspend fun deletePost(
        @Path("post_id") postId: Int
    ): Response<DeletePostResponse>

    @PUT("blog/{post_id}")
    suspend fun editPost(
        @Path("post_id") postId: Int,
        @Body request: PostRequest
    ): Response<PostResponse>

    @GET("blog")
    suspend fun getAllPosts(
        @Query("limit") limit: Int?
    ): Response<List<PostResponse>>

    @GET("blog/{post_id}")
    suspend fun getPostById(
        @Path("post_id") postId: Int?
    ): Response<PostResponse>

    @Multipart
    @POST("images/upload-image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<ImageUploadResponse>
}
package com.loc.searchapp.domain.usecases.posts

import com.loc.searchapp.data.remote.dto.CreatePostRequest
import com.loc.searchapp.data.remote.dto.PostResponse
import com.loc.searchapp.domain.repository.PostsRepository
import retrofit2.Response

class CreatePost(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(request: CreatePostRequest): Response<PostResponse> {
        return repository.createPost(request)
    }
}
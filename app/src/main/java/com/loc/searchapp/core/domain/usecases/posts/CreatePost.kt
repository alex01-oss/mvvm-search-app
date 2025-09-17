package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.data.remote.dto.CreatePostRequest
import com.loc.searchapp.core.data.remote.dto.PostResponse
import com.loc.searchapp.core.domain.repository.PostsRepository
import retrofit2.Response

class CreatePost(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(
        request: CreatePostRequest
    ): Response<PostResponse> {
        return repository.createPost(request)
    }
}
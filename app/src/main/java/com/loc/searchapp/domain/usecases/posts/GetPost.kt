package com.loc.searchapp.domain.usecases.posts

import com.loc.searchapp.data.remote.dto.PostResponse
import com.loc.searchapp.domain.repository.PostsRepository
import retrofit2.Response

class GetPost(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(
        postId: Int
    ): Response<PostResponse> {
        return repository.getPostById(postId)
    }
}
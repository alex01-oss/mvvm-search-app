package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.data.remote.dto.DeletePostResponse
import com.loc.searchapp.core.domain.repository.PostsRepository
import retrofit2.Response

class DeletePost(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(
        postId: Int
    ): Response<DeletePostResponse> {
        return repository.deletePost(postId)
    }
}
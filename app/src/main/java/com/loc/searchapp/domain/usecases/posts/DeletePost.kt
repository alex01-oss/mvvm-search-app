package com.loc.searchapp.domain.usecases.posts

import com.loc.searchapp.data.remote.dto.DeletePostResponse
import com.loc.searchapp.domain.repository.PostsRepository
import retrofit2.Response

class DeletePost(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(postId: Int): Response<DeletePostResponse> {
        return repository.deletePost(postId)
    }
}
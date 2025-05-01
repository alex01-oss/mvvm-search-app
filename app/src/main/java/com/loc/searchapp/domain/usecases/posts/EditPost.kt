package com.loc.searchapp.domain.usecases.posts

import com.loc.searchapp.data.remote.dto.EditPostRequest
import com.loc.searchapp.data.remote.dto.PostResponse
import com.loc.searchapp.domain.repository.PostsRepository
import retrofit2.Response

class EditPost(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(postId: Int, request: EditPostRequest): Response<PostResponse> {
        return repository.editPost(postId, request)
    }
}
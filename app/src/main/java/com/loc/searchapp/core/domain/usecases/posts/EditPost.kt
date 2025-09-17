package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.data.remote.dto.EditPostRequest
import com.loc.searchapp.core.data.remote.dto.PostResponse
import com.loc.searchapp.core.domain.repository.PostsRepository
import retrofit2.Response

class EditPost(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(
        postId: Int,
        request: EditPostRequest
    ): Response<PostResponse> {
        return repository.editPost(postId, request)
    }
}
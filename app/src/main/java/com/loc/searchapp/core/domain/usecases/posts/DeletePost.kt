package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.domain.model.posts.DeletePostResult
import com.loc.searchapp.core.domain.model.posts.PostId
import com.loc.searchapp.core.domain.repository.PostsRepository
import jakarta.inject.Inject

class DeletePost @Inject constructor(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(data: PostId): DeletePostResult {
        return repository.deletePost(data)
    }
}
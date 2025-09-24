package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.model.posts.PostId
import com.loc.searchapp.core.domain.repository.PostsRepository
import jakarta.inject.Inject

class GetPost @Inject constructor(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(data: PostId): Post {
        return repository.getPostById(data)
    }
}
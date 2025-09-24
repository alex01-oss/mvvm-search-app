package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.model.posts.PostData
import com.loc.searchapp.core.domain.repository.PostsRepository
import jakarta.inject.Inject

class CreatePost @Inject constructor(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(data: PostData): Post {
        return repository.createPost(data)
    }
}
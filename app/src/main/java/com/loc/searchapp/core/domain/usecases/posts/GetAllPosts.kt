package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.domain.model.catalog.Limit
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.repository.PostsRepository
import jakarta.inject.Inject

class GetAllPosts @Inject constructor(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(data: Limit?): List<Post> {
        return repository.getAllPosts(data)
    }
}
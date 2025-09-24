package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.domain.model.posts.EditPostData
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.repository.PostsRepository
import jakarta.inject.Inject

class EditPost @Inject constructor(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(data: EditPostData): Post {
        return repository.editPost(data)
    }
}
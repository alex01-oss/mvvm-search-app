package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.data.remote.dto.PostResponse
import com.loc.searchapp.core.domain.repository.PostsRepository
import retrofit2.Response

class GetAllPosts(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(): Response<List<PostResponse>> {
        return repository.getAllPosts()
    }
}
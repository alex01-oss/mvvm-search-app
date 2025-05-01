package com.loc.searchapp.domain.usecases.posts

import com.loc.searchapp.data.remote.dto.PostResponse
import com.loc.searchapp.domain.repository.PostsRepository
import retrofit2.Response

class GetAllPosts(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(): Response<List<PostResponse>> {
        return repository.getAllPosts()
    }
}
package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.domain.model.posts.UploadData
import com.loc.searchapp.core.domain.model.posts.UploadResult
import com.loc.searchapp.core.domain.repository.PostsRepository
import jakarta.inject.Inject

class UploadImage @Inject constructor(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(data: UploadData): UploadResult {
        return repository.uploadImage(data)
    }
}
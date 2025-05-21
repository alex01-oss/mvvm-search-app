package com.loc.searchapp.core.domain.usecases.posts

import com.loc.searchapp.core.data.remote.dto.ImageUploadResponse
import com.loc.searchapp.core.domain.repository.PostsRepository
import okhttp3.MultipartBody
import retrofit2.Response

class UploadImage(
    private val repository: PostsRepository
) {
    suspend operator fun invoke(file: MultipartBody.Part): Response<ImageUploadResponse> {
        return repository.uploadImage(file)
    }
}
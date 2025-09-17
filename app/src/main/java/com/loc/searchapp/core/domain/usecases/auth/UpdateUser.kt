package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.data.remote.dto.UpdateUserRequest
import com.loc.searchapp.core.data.remote.dto.UserResponse
import com.loc.searchapp.core.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class UpdateUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        request: UpdateUserRequest
    ): Response<UserResponse> {
        return authRepository.updateUser(request)
    }
}
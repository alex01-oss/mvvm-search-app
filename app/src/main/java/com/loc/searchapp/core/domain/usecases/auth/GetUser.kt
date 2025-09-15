package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.data.remote.dto.UserResponse
import com.loc.searchapp.core.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class GetUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Response<UserResponse> {
        return authRepository.getUser()
    }
}
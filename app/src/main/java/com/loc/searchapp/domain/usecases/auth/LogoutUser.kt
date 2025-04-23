package com.loc.searchapp.domain.usecases.auth

import com.loc.searchapp.data.remote.dto.LogoutResponse
import com.loc.searchapp.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        refreshToken: String
    ): Response<LogoutResponse> {
        return authRepository.logout("Bearer $refreshToken")
    }
}
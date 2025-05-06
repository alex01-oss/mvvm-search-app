package com.loc.searchapp.domain.usecases.auth

import com.loc.searchapp.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class RefreshToken @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        refreshToken: String
    ): Response<RefreshTokenResponse> {
        return authRepository.refresh(refreshToken)
    }
}
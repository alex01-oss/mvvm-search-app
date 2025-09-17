package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.data.remote.dto.RefreshTokenRequest
import com.loc.searchapp.core.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.core.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class RefreshToken @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        request: RefreshTokenRequest
    ): Response<RefreshTokenResponse> {
        return authRepository.refresh(request)
    }
}
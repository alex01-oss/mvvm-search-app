package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.data.remote.dto.LogoutRequest
import com.loc.searchapp.core.data.remote.dto.LogoutResponse
import com.loc.searchapp.core.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        request: LogoutRequest
    ): Response<LogoutResponse> {
        return authRepository.logout(request)
    }
}
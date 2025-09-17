package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.data.remote.dto.AuthResponse
import com.loc.searchapp.core.data.remote.dto.LoginRequest
import com.loc.searchapp.core.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class LoginUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        request: LoginRequest
    ): Response<AuthResponse> {
        return authRepository.login(request)
    }
}
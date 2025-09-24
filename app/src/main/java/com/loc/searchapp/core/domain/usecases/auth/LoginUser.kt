package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.model.auth.AuthResult
import com.loc.searchapp.core.domain.model.auth.LoginData
import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class LoginUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        data: LoginData
    ): AuthResult {
        return authRepository.login(data)
    }
}
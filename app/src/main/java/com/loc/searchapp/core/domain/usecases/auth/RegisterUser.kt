package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.model.auth.AuthResult
import com.loc.searchapp.core.domain.model.auth.RegisterData
import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class RegisterUser @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(
        data: RegisterData
    ): AuthResult {
        return authRepository.register(data)
    }
}
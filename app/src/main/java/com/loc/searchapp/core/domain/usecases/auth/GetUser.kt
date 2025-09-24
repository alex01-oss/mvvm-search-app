package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.model.auth.User
import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class GetUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User {
        return authRepository.getUser()
    }
}
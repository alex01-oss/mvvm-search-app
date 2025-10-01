package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.model.auth.LogoutResult
import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class LogoutAllDevices @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): LogoutResult? {
        return authRepository.logoutAllDevices()
    }
}
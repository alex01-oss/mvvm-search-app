package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.model.auth.LogoutResult
import com.loc.searchapp.core.domain.model.auth.RefreshData
import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class LogoutUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        data: RefreshData
    ): LogoutResult {
        return authRepository.logout(data)
    }
}
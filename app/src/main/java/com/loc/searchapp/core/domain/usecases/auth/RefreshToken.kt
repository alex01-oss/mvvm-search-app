package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.model.auth.RefreshData
import com.loc.searchapp.core.domain.model.auth.RefreshResult
import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class RefreshToken @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        data: RefreshData
    ): RefreshResult {
        return authRepository.refresh(data)
    }
}
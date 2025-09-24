package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class DeleteUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        return authRepository.deleteUser()
    }
}
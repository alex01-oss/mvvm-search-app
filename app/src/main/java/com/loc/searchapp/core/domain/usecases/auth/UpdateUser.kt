package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.model.auth.UpdateData
import com.loc.searchapp.core.domain.model.auth.User
import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class UpdateUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        data: UpdateData
    ): User {
        return authRepository.updateUser(data)
    }
}
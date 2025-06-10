package com.loc.searchapp.core.domain.usecases.auth

import com.loc.searchapp.core.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class DeleteUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String): Response<Unit> {
        return authRepository.deleteUser("Bearer $token")
    }
}
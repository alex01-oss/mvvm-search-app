package com.loc.searchapp.domain.usecases.auth

import com.loc.searchapp.data.network.dto.AuthResponse
import com.loc.searchapp.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class RegisterUser @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Response<AuthResponse> {
        return authRepository.register(username, email, password)
    }
}
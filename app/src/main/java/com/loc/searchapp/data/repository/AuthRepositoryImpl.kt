package com.loc.searchapp.data.repository

import com.loc.searchapp.data.network.AuthApi
import com.loc.searchapp.data.network.dto.AuthResponse
import com.loc.searchapp.data.network.dto.LoginRequest
import com.loc.searchapp.data.network.dto.LogoutRequest
import com.loc.searchapp.data.network.dto.RegisterRequest
import com.loc.searchapp.domain.repository.AuthRepository
import retrofit2.Response

class AuthRepositoryImpl(
    private val api: AuthApi,
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Response<AuthResponse> {
        return api.login(
            LoginRequest(email, password)
        )
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Response<AuthResponse> {
        return api.register(
            RegisterRequest(username, email, password)
        )
    }

    override suspend fun refresh(
        token: String
    ): Response<AuthResponse> {
        return api.refresh(token)
    }

    override suspend fun getUser(
        token: String
    ): Response<AuthResponse> {
        return api.getUser(token)
    }

    override suspend fun logout(
        refreshToken: String
    ): Response<AuthResponse> {
        return api.logout(refreshToken)
    }
}
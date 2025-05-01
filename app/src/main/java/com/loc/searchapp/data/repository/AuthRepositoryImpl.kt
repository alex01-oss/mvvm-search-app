package com.loc.searchapp.data.repository

import com.loc.searchapp.data.remote.api.AuthApi
import com.loc.searchapp.data.remote.dto.AuthResponse
import com.loc.searchapp.data.remote.dto.LoginRequest
import com.loc.searchapp.data.remote.dto.LogoutRequest
import com.loc.searchapp.data.remote.dto.LogoutResponse
import com.loc.searchapp.data.remote.dto.RefreshTokenRequest
import com.loc.searchapp.data.remote.dto.RegisterRequest
import com.loc.searchapp.data.remote.dto.UserResponse
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
        refreshToken: String
    ): Response<AuthResponse> {
        return api.refresh(
            RefreshTokenRequest(refreshToken)
        )
    }

    override suspend fun getUser(
        accessToken: String
    ): Response<UserResponse> {
        return api.getUser(
            accessToken
        )
    }

    override suspend fun logout(
        accessToken: String,
        refreshToken: String
    ): Response<LogoutResponse> {
        return api.logout(
            accessToken,
            LogoutRequest(refreshToken)
        )
    }
}
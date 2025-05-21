package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.remote.api.AuthApi
import com.loc.searchapp.core.data.remote.dto.AuthResponse
import com.loc.searchapp.core.data.remote.dto.LoginRequest
import com.loc.searchapp.core.data.remote.dto.LogoutRequest
import com.loc.searchapp.core.data.remote.dto.LogoutResponse
import com.loc.searchapp.core.data.remote.dto.RefreshTokenRequest
import com.loc.searchapp.core.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.core.data.remote.dto.RegisterRequest
import com.loc.searchapp.core.data.remote.dto.UserResponse
import com.loc.searchapp.core.domain.repository.AuthRepository
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
    ): Response<RefreshTokenResponse> {
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
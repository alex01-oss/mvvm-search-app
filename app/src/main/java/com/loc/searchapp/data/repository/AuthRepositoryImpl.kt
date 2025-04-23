package com.loc.searchapp.data.repository

import com.loc.searchapp.data.remote.api.AuthApi
import com.loc.searchapp.data.remote.dto.AuthResponse
import com.loc.searchapp.data.remote.dto.LoginRequest
import com.loc.searchapp.data.remote.dto.RegisterRequest
import com.loc.searchapp.data.remote.dto.UserResponse
import com.loc.searchapp.domain.repository.AuthRepository
import com.loc.searchapp.data.remote.dto.LogoutResponse

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
    ): Response<UserResponse> {
        return api.getUser(token)
    }

    override suspend fun logout(
        refreshToken: String
    ): Response<LogoutResponse> {
        return api.logout(refreshToken)
    }
}
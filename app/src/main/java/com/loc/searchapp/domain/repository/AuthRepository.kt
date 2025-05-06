package com.loc.searchapp.domain.repository

import com.loc.searchapp.data.remote.dto.AuthResponse
import com.loc.searchapp.data.remote.dto.LogoutResponse
import com.loc.searchapp.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.data.remote.dto.UserResponse
import retrofit2.Response

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Response<AuthResponse>

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): Response<AuthResponse>

    suspend fun refresh(
        refreshToken: String
    ): Response<RefreshTokenResponse>

    suspend fun getUser(
        accessToken: String
    ): Response<UserResponse>

    suspend fun logout(
        accessToken: String,
        refreshToken: String
    ): Response<LogoutResponse>
}
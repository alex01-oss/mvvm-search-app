package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.AuthResponse
import com.loc.searchapp.core.data.remote.dto.LogoutResponse
import com.loc.searchapp.core.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.core.data.remote.dto.UpdateUserRequest
import com.loc.searchapp.core.data.remote.dto.UserResponse
import retrofit2.Response

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Response<AuthResponse>

    suspend fun register(
        fullname: String,
        email: String,
        phone: String,
        password: String
    ): Response<AuthResponse>

    suspend fun refresh(
        refreshToken: String
    ): Response<RefreshTokenResponse>

    suspend fun getUser(): Response<UserResponse>

    suspend fun updateUser(
        updateUserRequest: UpdateUserRequest
    ): Response<UserResponse>

    suspend fun deleteUser(): Response<Unit>

    suspend fun logout(
        refreshToken: String
    ): Response<LogoutResponse>
}
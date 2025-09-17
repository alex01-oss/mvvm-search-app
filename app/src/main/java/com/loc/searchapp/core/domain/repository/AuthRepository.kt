package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.data.remote.dto.AuthResponse
import com.loc.searchapp.core.data.remote.dto.LoginRequest
import com.loc.searchapp.core.data.remote.dto.LogoutRequest
import com.loc.searchapp.core.data.remote.dto.LogoutResponse
import com.loc.searchapp.core.data.remote.dto.RefreshTokenRequest
import com.loc.searchapp.core.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.core.data.remote.dto.RegisterRequest
import com.loc.searchapp.core.data.remote.dto.UpdateUserRequest
import com.loc.searchapp.core.data.remote.dto.UserResponse
import retrofit2.Response

interface AuthRepository {
    suspend fun login(
        request: LoginRequest
    ): Response<AuthResponse>

    suspend fun register(
        request: RegisterRequest
    ): Response<AuthResponse>

    suspend fun refresh(
        request: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    suspend fun getUser(): Response<UserResponse>

    suspend fun updateUser(
        request: UpdateUserRequest
    ): Response<UserResponse>

    suspend fun deleteUser(): Response<Unit>

    suspend fun logout(
        request: LogoutRequest
    ): Response<LogoutResponse>
}
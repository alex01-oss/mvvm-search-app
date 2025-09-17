package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.remote.api.AuthApi
import com.loc.searchapp.core.data.remote.dto.AuthResponse
import com.loc.searchapp.core.data.remote.dto.LoginRequest
import com.loc.searchapp.core.data.remote.dto.LogoutRequest
import com.loc.searchapp.core.data.remote.dto.LogoutResponse
import com.loc.searchapp.core.data.remote.dto.RefreshTokenRequest
import com.loc.searchapp.core.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.core.data.remote.dto.RegisterRequest
import com.loc.searchapp.core.data.remote.dto.UpdateUserRequest
import com.loc.searchapp.core.data.remote.dto.UserResponse
import com.loc.searchapp.core.domain.repository.AuthRepository
import retrofit2.Response

class AuthRepositoryImpl(
    private val api: AuthApi,
) : AuthRepository {

    override suspend fun login(
        request: LoginRequest
    ): Response<AuthResponse> {
        return api.login(request)
    }

    override suspend fun register(
        request: RegisterRequest
    ): Response<AuthResponse> {
        return api.register(request)
    }

    override suspend fun refresh(
        request: RefreshTokenRequest
    ): Response<RefreshTokenResponse> {
        return api.refresh(request)
    }

    override suspend fun getUser(): Response<UserResponse> {
        return api.getUser()
    }

    override suspend fun updateUser(
        request: UpdateUserRequest
    ): Response<UserResponse> {
        return api.updateUser(request)
    }

    override suspend fun deleteUser(): Response<Unit> {
        return api.deleteUser()
    }

    override suspend fun logout(
        request: LogoutRequest
    ): Response<LogoutResponse> {
        return api.logout(request)
    }
}
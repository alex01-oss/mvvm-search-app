package com.loc.searchapp.core.data.repository

import android.util.Log
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
        email: String,
        password: String
    ): Response<AuthResponse> {
        Log.d("AUTH", "About to call API")
        return api.login(
            LoginRequest(email, password)
        )
    }

    override suspend fun register(
        fullname: String,
        email: String,
        phone: String,
        password: String
    ): Response<AuthResponse> {
        return api.register(
            RegisterRequest(fullname, email, phone, password)
        )
    }

    override suspend fun refresh(
        refreshToken: String
    ): Response<RefreshTokenResponse> {
        return api.refresh(
            RefreshTokenRequest(refreshToken)
        )
    }

    override suspend fun getUser(): Response<UserResponse> {
        return api.getUser()
    }

    override suspend fun updateUser(
        updateUserRequest: UpdateUserRequest
    ): Response<UserResponse> {
        return api.updateUser(updateUserRequest)
    }

    override suspend fun deleteUser(): Response<Unit> {
        return api.deleteUser()
    }

    override suspend fun logout(
        refreshToken: String
    ): Response<LogoutResponse> {
        return api.logout(
            LogoutRequest(refreshToken)
        )
    }
}
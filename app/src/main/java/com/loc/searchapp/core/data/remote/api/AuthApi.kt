package com.loc.searchapp.core.data.remote.api

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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Body request: LogoutRequest
    ): Response<LogoutResponse>

    @POST("auth/refresh")
    suspend fun refresh(
        @Body request: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @PATCH("/api/user")
    suspend fun updateUser(
        @Body updateUserRequest: UpdateUserRequest
    ): Response<UserResponse>

    @DELETE("api/user")
    suspend fun deleteUser(): Response<Unit>

    @GET("user")
    suspend fun getUser(): Response<UserResponse>
}
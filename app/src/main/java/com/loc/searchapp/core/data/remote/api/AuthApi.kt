package com.loc.searchapp.core.data.remote.api

import com.loc.searchapp.core.data.remote.dto.AuthResponse
import com.loc.searchapp.core.data.remote.dto.LoginRequest
import com.loc.searchapp.core.data.remote.dto.LogoutRequest
import com.loc.searchapp.core.data.remote.dto.LogoutResponse
import com.loc.searchapp.core.data.remote.dto.RefreshTokenRequest
import com.loc.searchapp.core.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.core.data.remote.dto.RegisterRequest
import com.loc.searchapp.core.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/logout")
    suspend fun logout(
        @Header("Authorization") accessToken: String,
        @Body request: LogoutRequest
    ): Response<LogoutResponse>

    @POST("api/refresh")
    suspend fun refresh(
        @Body request: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @GET("api/user")
    suspend fun getUser(
        @Header("Authorization") accessToken: String
    ): Response<UserResponse>
}
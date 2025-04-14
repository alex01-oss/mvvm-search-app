package com.loc.searchapp.data.network

import com.loc.searchapp.data.network.dto.AuthResponse
import com.loc.searchapp.data.network.dto.LoginRequest
import com.loc.searchapp.data.network.dto.RegisterRequest
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
        @Header("Authorization") refreshToken: String
    ): Response<AuthResponse>

    @POST("api/refresh")
    suspend fun refresh(
        @Header("Authorization") refreshToken: String
    ): Response<AuthResponse>

    @GET("api/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<AuthResponse>
}
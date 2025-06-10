package com.loc.searchapp.core.data.remote.dto

import com.loc.searchapp.core.domain.model.auth.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val fullname: String,
    val email: String,
    val phone: String,
    val password: String
)

@Serializable
data class AuthResponse (
    val message: String? = null,
    val user: User,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
data class UserResponse (
    val user: User
)

@Serializable
data class LogoutRequest(
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
data class LogoutResponse(
    val message: String
)

@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
data class RefreshTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String
)

@Serializable
data class UpdateUserRequest(
    @SerialName("full_name") val fullname: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val password: String? = null
)
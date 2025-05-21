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
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse (
    val message: String? = null,
    val user: User,
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class UserResponse (
    val user: User
)

@Serializable
data class LogoutRequest(
    val refreshToken: String
)

@Serializable
data class LogoutResponse(
    val message: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    @SerialName("token_type") val tokenType: String
)
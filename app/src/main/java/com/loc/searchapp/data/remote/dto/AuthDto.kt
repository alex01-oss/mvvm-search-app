package com.loc.searchapp.data.remote.dto

import com.loc.searchapp.domain.model.User
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
    val token: String,
    val refreshToken: String
)

@Serializable
data class UserResponse (
    val user: User
)

@Serializable
data class LogoutResponse(
    val message: String
)

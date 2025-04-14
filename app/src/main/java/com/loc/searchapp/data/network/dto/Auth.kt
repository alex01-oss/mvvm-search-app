package com.loc.searchapp.data.network.dto

import com.loc.searchapp.domain.model.User

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class AuthResponse (
    val message: String,
    val user: User,
    val token: String,
    val refreshToken: String
)

data class LogoutRequest(
    val refreshToken: String
)
package com.loc.searchapp.data.network.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class RefreshRequest(
    val token: String
)
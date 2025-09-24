package com.loc.searchapp.core.domain.model.auth

data class User(
    val id: String,
    val fullname: String,
    val email: String,
    val phone: String,
    val role: String
)

data class LoginData(
    val email: String,
    val password: String
)

data class RegisterData(
    val fullname: String,
    val email: String,
    val phone: String,
    val password: String
)

data class UpdateData(
    val fullname: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val password: String? = null
)

data class RefreshData(
    val refreshToken: String
)

data class AuthResult(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)

data class RefreshResult(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)

data class LogoutResult(
    val message: String
)
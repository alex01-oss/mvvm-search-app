package com.loc.searchapp.core.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    @SerialName("full_name") val fullname: String,
    val email: String,
    val phone: String,
    val password: String
)

@Serializable
data class AuthResponse (
    val user: UserDto,
    val message: String? = null,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String
)

@Serializable
data class UserResponse(
    val user: UserDto
)
@Serializable
@Parcelize
data class UserDto(
    val id: String,
    @SerialName("full_name") val fullname: String,
    val email: String,
    val phone: String,
    val role: String
) : Parcelable

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
    val password: String? = null,
)
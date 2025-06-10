package com.loc.searchapp.feature.shared.model

sealed class AuthEvent {
    data class LoginUser(
        val email: String,
        val password: String
    ) : AuthEvent()

    data class RegisterUser(
        val fullname: String,
        val email: String,
        val phone: String,
        val password: String
    ) : AuthEvent()

    data class UpdateUser(
        val fullname: String,
        val email: String,
        val phone: String,
        val currentPassword: String?,
        val newPassword: String?
    ) : AuthEvent()

    data object DeleteUser : AuthEvent()

    data object LogoutUser : AuthEvent()
}
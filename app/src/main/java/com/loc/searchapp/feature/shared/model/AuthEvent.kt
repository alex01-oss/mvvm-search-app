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

    data object LogoutUser : AuthEvent()
}
package com.loc.searchapp.feature.auth.model

sealed class AuthEvent {
    data class LoginUser(
        val email: String,
        val password: String
    ) : AuthEvent()

    data class RegisterUser(
        val username: String,
        val email: String,
        val password: String
    ) : AuthEvent()

    object LogoutUser : AuthEvent()
}
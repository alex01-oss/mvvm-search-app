package com.loc.searchapp.presentation.auth

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

    class LogoutUser() : AuthEvent()
}
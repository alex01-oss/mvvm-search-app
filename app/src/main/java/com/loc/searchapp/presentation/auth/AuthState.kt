package com.loc.searchapp.presentation.auth

import com.loc.searchapp.domain.model.User

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: User?) : AuthState()
    data class Error(val message: String) : AuthState()
}
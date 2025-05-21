package com.loc.searchapp.feature.auth.model

import com.loc.searchapp.core.domain.model.auth.User

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: User?) : AuthState()
    data class Error(val message: String) : AuthState()
}
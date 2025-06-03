package com.loc.searchapp.feature.shared.model

import com.loc.searchapp.core.domain.model.auth.User

sealed class AuthState {
    data object Loading : AuthState()
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: User?) : AuthState()
    data class Error(val message: String) : AuthState()
}
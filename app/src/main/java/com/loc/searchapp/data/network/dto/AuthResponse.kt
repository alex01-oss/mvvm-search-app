package com.loc.searchapp.data.network.dto

import com.loc.searchapp.domain.model.User

data class AuthResponse (
    val message: String,
    val user: User,
    val token: String,
    val refreshToken: String
)
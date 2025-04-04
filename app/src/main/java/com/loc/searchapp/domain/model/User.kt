package com.loc.searchapp.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val token: String,
    val refreshToken: String
)

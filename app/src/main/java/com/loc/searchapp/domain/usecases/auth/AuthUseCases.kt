package com.loc.searchapp.domain.usecases.auth

data class AuthUseCases(
    val loginUser: LoginUser,
    val registerUser: RegisterUser,
    val refreshToken: RefreshToken
)

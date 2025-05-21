package com.loc.searchapp.core.domain.usecases.auth

data class AuthUseCases(
    val loginUser: LoginUser,
    val registerUser: RegisterUser,
    val refreshToken: RefreshToken,
    val logoutUser: LogoutUser,
    val getUser: GetUser
)

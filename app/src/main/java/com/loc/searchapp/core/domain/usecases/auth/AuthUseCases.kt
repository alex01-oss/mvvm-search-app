package com.loc.searchapp.core.domain.usecases.auth

import jakarta.inject.Inject

data class AuthUseCases @Inject constructor(
    val loginUser: LoginUser,
    val registerUser: RegisterUser,
    val refreshToken: RefreshToken,
    val updateUser: UpdateUser,
    val logoutUser: LogoutUser,
    val getUser: GetUser,
    val deleteUser: DeleteUser
)

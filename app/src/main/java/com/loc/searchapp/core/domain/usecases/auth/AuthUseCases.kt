package com.loc.searchapp.core.domain.usecases.auth

data class AuthUseCases(
    val loginUser: LoginUser,
    val registerUser: RegisterUser,
    val refreshToken: RefreshToken,
    val updateUser: UpdateUser,
    val logoutUser: LogoutUser,
    val getUser: GetUser,
    val deleteUser: DeleteUser
)

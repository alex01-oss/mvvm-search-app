package com.loc.searchapp.core.data.mappers

import com.loc.searchapp.core.data.remote.dto.AuthResponse
import com.loc.searchapp.core.data.remote.dto.LoginRequest
import com.loc.searchapp.core.data.remote.dto.LogoutResponse
import com.loc.searchapp.core.data.remote.dto.RefreshTokenRequest
import com.loc.searchapp.core.data.remote.dto.RefreshTokenResponse
import com.loc.searchapp.core.data.remote.dto.RegisterRequest
import com.loc.searchapp.core.data.remote.dto.UpdateUserRequest
import com.loc.searchapp.core.data.remote.dto.UserDto
import com.loc.searchapp.core.data.remote.dto.UserResponse
import com.loc.searchapp.core.domain.model.auth.AuthResult
import com.loc.searchapp.core.domain.model.auth.LoginData
import com.loc.searchapp.core.domain.model.auth.LogoutResult
import com.loc.searchapp.core.domain.model.auth.RefreshData
import com.loc.searchapp.core.domain.model.auth.RefreshResult
import com.loc.searchapp.core.domain.model.auth.RegisterData
import com.loc.searchapp.core.domain.model.auth.UpdateData
import com.loc.searchapp.core.domain.model.auth.User

fun AuthResponse.toDomain(): AuthResult {
    return AuthResult(
        user = this.user.toDomain(),
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        tokenType = this.tokenType
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        fullname = this.fullname,
        email = this.email,
        phone = this.phone,
        role = this.role
    )
}

fun RefreshTokenResponse.toDomain(): RefreshResult {
    return RefreshResult(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        tokenType = this.tokenType
    )
}

fun LogoutResponse.toDomain(): LogoutResult {
    return LogoutResult(
        message = this.message
    )
}

fun LoginData.toDto(): LoginRequest {
    return LoginRequest(
        email = this.email,
        password = this.password
    )
}

fun RegisterData.toDto(): RegisterRequest {
    return RegisterRequest(
        fullname = this.fullname,
        email = this.email,
        phone = this.phone,
        password = this.password
    )
}

fun RefreshData.toDto(): RefreshTokenRequest {
    return RefreshTokenRequest(
        refreshToken = this.refreshToken
    )
}

fun UpdateData.toDto(): UpdateUserRequest {
    return UpdateUserRequest(
        fullname = this.fullname,
        email = this.email,
        phone = this.phone,
        password = this.password
    )
}
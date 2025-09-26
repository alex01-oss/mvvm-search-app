package com.loc.searchapp.core.data.repository

import com.loc.searchapp.core.data.mappers.toDomain
import com.loc.searchapp.core.data.mappers.toDto
import com.loc.searchapp.core.data.remote.api.AuthApi
import com.loc.searchapp.core.domain.model.auth.AuthResult
import com.loc.searchapp.core.domain.model.auth.LoginData
import com.loc.searchapp.core.domain.model.auth.LogoutResult
import com.loc.searchapp.core.domain.model.auth.RefreshData
import com.loc.searchapp.core.domain.model.auth.RefreshResult
import com.loc.searchapp.core.domain.model.auth.RegisterData
import com.loc.searchapp.core.domain.model.auth.UpdateData
import com.loc.searchapp.core.domain.model.auth.User
import com.loc.searchapp.core.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
) : AuthRepository {

    override suspend fun login(data: LoginData): AuthResult {
        val res = api.login(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Login successful but response body is null")
        } else {
            throw RuntimeException("Login failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun register(data: RegisterData): AuthResult {
        val res = api.register(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Registration successful but response body is null")
        } else {
            throw RuntimeException("Registration failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun refresh(data: RefreshData): RefreshResult {
        val res = api.refresh(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Refresh token successful but body is null")
        } else {
            throw RuntimeException("Refresh token failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun getUser(): User {
        val res = api.getUser()
        return if (res.isSuccessful) {
            res.body()?.user?.toDomain() ?: throw RuntimeException("Get user successful but body is null")
        } else {
            throw RuntimeException("Get user failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun updateUser(data: UpdateData): User {
        val res = api.updateUser(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain() ?: throw RuntimeException("Update user successful but body is null")
        } else {
            throw RuntimeException("Update user failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun deleteUser() {
        val res = api.deleteUser()
        if (!res.isSuccessful) {
            throw RuntimeException("Delete user failed: ${res.code()} ${res.message()}")
        }
    }

    override suspend fun logout(data: RefreshData): LogoutResult? {
        val res = api.logout(data.toDto())
        return if (res.isSuccessful) {
            res.body()?.toDomain()
        } else {
            throw RuntimeException("Logout failed: ${res.code()} ${res.message()}")
        }
    }
}
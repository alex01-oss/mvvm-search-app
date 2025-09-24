package com.loc.searchapp.core.domain.repository

import com.loc.searchapp.core.domain.model.auth.AuthResult
import com.loc.searchapp.core.domain.model.auth.LoginData
import com.loc.searchapp.core.domain.model.auth.LogoutResult
import com.loc.searchapp.core.domain.model.auth.RefreshData
import com.loc.searchapp.core.domain.model.auth.RefreshResult
import com.loc.searchapp.core.domain.model.auth.RegisterData
import com.loc.searchapp.core.domain.model.auth.UpdateData
import com.loc.searchapp.core.domain.model.auth.User

interface AuthRepository {
    suspend fun login(data: LoginData): AuthResult
    suspend fun register(data: RegisterData): AuthResult
    suspend fun refresh(data: RefreshData): RefreshResult
    suspend fun getUser(): User
    suspend fun updateUser(data: UpdateData): User
    suspend fun deleteUser()
    suspend fun logout(data: RefreshData): LogoutResult
}
package com.loc.searchapp.presentation.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.data.remote.dto.LoginRequest
import com.loc.searchapp.core.data.remote.dto.LogoutRequest
import com.loc.searchapp.core.data.remote.dto.RegisterRequest
import com.loc.searchapp.core.data.remote.dto.UpdateUserRequest
import com.loc.searchapp.core.domain.usecases.auth.AuthUseCases
import com.loc.searchapp.presentation.shared.model.AuthEvent
import com.loc.searchapp.presentation.shared.model.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val isAdmin: Boolean
        get() = (authState.value as? AuthState.Authenticated)?.user?.role == "admin"

    private val _logoutCompleted = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            val hasTokens = userPreferences.getAccessToken() != null
            if (hasTokens) {
                fetchUserAndSetState()
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    private suspend fun fetchUserAndSetState(): Boolean {
        return try {
            val response = authUseCases.getUser()
            val user = response.body()?.user
            if (response.isSuccessful && user != null) {
                _authState.value = AuthState.Authenticated(user)
                true
            } else {
                userPreferences.clearTokens()
                _authState.value = AuthState.Unauthenticated
                false
            }
        } catch (_: Exception) {
            userPreferences.clearTokens()
            _authState.value = AuthState.Unauthenticated
            false
        }
    }

    fun clearError() {
        _authState.value = AuthState.Unauthenticated
    }

    fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }

    fun onEvent(event: AuthEvent) {
        viewModelScope.launch {
            when (event) {
                is AuthEvent.LoginUser -> handleLogin(event.email, event.password)
                is AuthEvent.RegisterUser -> handleRegister(event.fullname, event.email, event.phone, event.password)
                is AuthEvent.UpdateUser -> handleUpdateUser(event)
                is AuthEvent.DeleteUser -> handleDeleteUser()
                is AuthEvent.LogoutUser -> handleLogout()
            }
        }
    }

    private suspend fun handleLogin(email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            val request = LoginRequest(email, password)
            val response = authUseCases.loginUser(request)
            if (response.isSuccessful) {
                val data = response.body()
                if (data?.accessToken.isNullOrBlank() || data.refreshToken.isBlank()) {
                    _authState.value = AuthState.Error("Invalid tokens received")
                    return
                }

                userPreferences.saveTokens(
                    accessToken = data.accessToken,
                    refreshToken = data.refreshToken
                )

                val savedToken = userPreferences.getAccessToken()
                if (savedToken.isNullOrBlank()) {
                    _authState.value = AuthState.Error("Failed to save tokens")
                    return
                }

                val user = data.user
                _authState.value = AuthState.Authenticated(user)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Invalid credentials"
                    422 -> "Invalid input data"
                    else -> "Login failed: ${response.code()}"
                }
                _authState.value = AuthState.Error(errorMsg)
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Network error")
        }
    }

    private suspend fun handleRegister(fullname: String, email: String, phone: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            val request = RegisterRequest(fullname, email, phone, password)
            val response = authUseCases.registerUser(request)
            if (response.isSuccessful) {
                val data = response.body()

                if (data?.accessToken.isNullOrBlank() || data.refreshToken.isBlank()) {
                    _authState.value = AuthState.Error("Invalid tokens received")
                    return
                }

                userPreferences.saveTokens(
                    data.accessToken,
                    data.refreshToken
                )

                val savedToken = userPreferences.getAccessToken()
                if (savedToken.isNullOrBlank()) {
                    _authState.value = AuthState.Error("Failed to save tokens")
                    return
                }

                val user = data.user
                _authState.value = AuthState.Authenticated(user)
            } else {
                val errorMsg = when (response.code()) {
                    409 -> "Email already exists"
                    422 -> "Invalid input data"
                    else -> "Registration failed: ${response.code()}"
                }
                _authState.value = AuthState.Error(errorMsg)
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    private suspend fun handleUpdateUser(event: AuthEvent.UpdateUser) {
        _authState.value = AuthState.Loading
        try {
            val request = UpdateUserRequest(
                fullname = event.fullname,
                email = event.email,
                phone = event.phone,
                password = event.newPassword
            )

            val response = authUseCases.updateUser(request)

            if (response.isSuccessful) {
                val updatedUser = response.body()?.user
                if (updatedUser != null) {
                    _authState.value = AuthState.Authenticated(updatedUser)
                } else {
                    _authState.value = AuthState.Error("Failed to update user")
                }
            } else {
                _authState.value = AuthState.Error("Update failed: ${response.message()}")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    private suspend fun handleDeleteUser() {
        try {
            val result = authUseCases.deleteUser()
            if (result.isSuccessful) {
                userPreferences.clearTokens()
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.Error("Failed to delete account")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Unexpected error: ${e.localizedMessage}")
        }
    }

    private suspend fun handleLogout() {
        try {
            val refreshToken = userPreferences.getRefreshToken()
            if (!refreshToken.isNullOrEmpty()) {
                try {
                    val request = LogoutRequest(refreshToken)
                    authUseCases.logoutUser(request)
                } catch (_: Exception) {}
            }
        } finally {
            userPreferences.clearTokens()
            _authState.value = AuthState.Unauthenticated
            _logoutCompleted.value = true
        }
    }
}
package com.loc.searchapp.feature.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.domain.usecases.auth.AuthUseCases
import com.loc.searchapp.feature.shared.model.AuthEvent
import com.loc.searchapp.feature.shared.model.AuthState
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
            fetchUserAndSetState()
        }
    }

    private suspend fun fetchUserAndSetState(): Boolean {
        val token = userPreferences.getAccessToken()
        if (token.isNullOrEmpty()) {
            _authState.value = AuthState.Unauthenticated
            return false
        }

        return try {
            val response = authUseCases.getUser(token)
            val user = response.body()?.user
            if (response.isSuccessful && user != null) {
                _authState.value = AuthState.Authenticated(user)
                true
            } else {
                _authState.value = AuthState.Unauthenticated
                false
            }
        } catch (_: Exception) {
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
            val response = authUseCases.loginUser(email, password)
            if (response.isSuccessful) {
                val data = response.body()
                userPreferences.saveTokens(
                    data?.accessToken.orEmpty(),
                    data?.refreshToken.orEmpty()
                )
                if (!fetchUserAndSetState()) {
                    _authState.value = AuthState.Error("Login succeeded but failed to load user")
                }
            } else {
                _authState.value = AuthState.Error("Invalid credentials")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    private suspend fun handleRegister(fullname: String, email: String, phone: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            val response = authUseCases.registerUser(fullname, email, phone, password)
            if (response.isSuccessful) {
                val data = response.body()
                userPreferences.saveTokens(
                    data?.accessToken.orEmpty(),
                    data?.refreshToken.orEmpty()
                )
                if (!fetchUserAndSetState()) {
                    _authState.value = AuthState.Error("Registration succeeded but failed to load user")
                }
            } else {
                _authState.value = AuthState.Error("Registration failed")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    private suspend fun handleUpdateUser(event: AuthEvent.UpdateUser) {
        _authState.value = AuthState.Loading
        try {
            val accessToken = userPreferences.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                _authState.value = AuthState.Unauthenticated
                return
            }

            val response = authUseCases.updateUser(
                accessToken = accessToken,
                fullname = event.fullname,
                email = event.email,
                phone = event.phone,
                password = event.newPassword
            )

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
            val token = userPreferences.getAccessToken().orEmpty()
            val result = authUseCases.deleteUser(token)
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
            val accessToken = userPreferences.getAccessToken()
            val refreshToken = userPreferences.getRefreshToken()
            if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                authUseCases.logoutUser(accessToken, refreshToken)
            }
        } catch (_: Exception) {
            // Ignore logout errors
        } finally {
            userPreferences.clearTokens()
            _authState.value = AuthState.Unauthenticated
            _logoutCompleted.value = true
        }
    }
}
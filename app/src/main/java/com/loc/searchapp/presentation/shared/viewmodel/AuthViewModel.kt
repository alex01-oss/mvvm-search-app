package com.loc.searchapp.presentation.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.domain.model.auth.LoginData
import com.loc.searchapp.core.domain.model.auth.RefreshData
import com.loc.searchapp.core.domain.model.auth.RegisterData
import com.loc.searchapp.core.domain.model.auth.UpdateData
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
            try {
                val accessToken = userPreferences.getAccessToken()
                if (!accessToken.isNullOrBlank()) fetchUserAndSetState() else _authState.value = AuthState.Unauthenticated
            } catch (_: Exception) {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    private suspend fun fetchUserAndSetState() {
        try {
            val user = authUseCases.getUser()
            _authState.value = AuthState.Authenticated(user)
        } catch (_: Exception) {
            userPreferences.clearTokens()
            _authState.value = AuthState.Unauthenticated
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
                is AuthEvent.RegisterUser -> handleRegister(
                    event.fullname,
                    event.email,
                    event.phone,
                    event.password
                )

                is AuthEvent.UpdateUser -> handleUpdateUser(event)
                is AuthEvent.DeleteUser -> handleDeleteUser()
                is AuthEvent.LogoutUser -> handleLogout()
                AuthEvent.LogoutAllDevices -> handleLogoutAll()
            }
        }
    }

    private suspend fun handleLogin(email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            val data = authUseCases.loginUser(LoginData(email, password))
            userPreferences.saveTokens(data.accessToken, data.refreshToken)
            _authState.value = AuthState.Authenticated(data.user)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Network error")
        }
    }

    private suspend fun handleRegister(
        fullname: String,
        email: String,
        phone: String,
        password: String
    ) {
        _authState.value = AuthState.Loading
        try {
            val data = authUseCases.registerUser(RegisterData(fullname, email, phone, password))
            userPreferences.saveTokens(data.accessToken, data.refreshToken)
            _authState.value = AuthState.Authenticated(data.user)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    private suspend fun handleUpdateUser(event: AuthEvent.UpdateUser) {
        _authState.value = AuthState.Loading
        try {
            authUseCases.updateUser(
                UpdateData(
                    event.fullname,
                    event.email,
                    event.phone,
                    event.password
                )
            )

            val updatedUser = authUseCases.getUser()

            _authState.value = AuthState.Authenticated(updatedUser)
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    private suspend fun handleDeleteUser() {
        try {
            authUseCases.deleteUser()
            userPreferences.clearTokens()
            _authState.value = AuthState.Unauthenticated
        } catch (e: Exception) {
            _authState.value = AuthState.Error("Unexpected error: ${e.localizedMessage}")
        }
    }

    private suspend fun handleLogout() {
        try {
            userPreferences.getRefreshToken()?.let {
                authUseCases.logoutUser(RefreshData(it))
            }
        } finally {
            userPreferences.clearTokens()
            _authState.value = AuthState.Unauthenticated
            _logoutCompleted.value = true
        }
    }

    private suspend fun handleLogoutAll() {
        try {
            authUseCases.logoutAllDevices()
        } finally {
            userPreferences.clearTokens()
            _authState.value = AuthState.Unauthenticated
            _logoutCompleted.value = true
        }
    }
}
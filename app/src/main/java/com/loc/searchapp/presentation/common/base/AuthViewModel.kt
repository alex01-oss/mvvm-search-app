package com.loc.searchapp.presentation.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.data.local.preferences.UserPreferences
import com.loc.searchapp.domain.usecases.auth.AuthUseCases
import com.loc.searchapp.presentation.auth.AuthEvent
import com.loc.searchapp.presentation.auth.AuthState
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

    private val _logoutCompleted = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            val token = userPreferences.getToken()

            if (token.isNullOrEmpty()) {
                _authState.value = AuthState.Unauthenticated
                return@launch
            }

            try {
                val response = authUseCases.getUser(token)
                val body = response.body()
                val user = body?.user

                if (response.isSuccessful && user != null) {
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (_: Exception) {
                _authState.value = AuthState.Unauthenticated
            }

        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.LoginUser -> {
                viewModelScope.launch {
                    _authState.value = AuthState.Loading

                    try {
                        val response = authUseCases.loginUser(
                            event.email,
                            event.password
                        )

                        if (response.isSuccessful) {
                            val data = response.body()
                            val token = data?.token.orEmpty()
                            val refreshToken = data?.refreshToken.orEmpty()
                            val user = data?.user

                            if (response.isSuccessful && user != null) {
                                userPreferences.saveTokens(token, refreshToken)
                                _authState.value = AuthState.Authenticated(user)
                            } else {
                                _authState.value = AuthState.Error("Invalid login or missing user data")
                            }
                        } else {
                            _authState.value = AuthState.Error("Invalid credentials")
                        }
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
                    }
                }
            }

            is AuthEvent.RegisterUser -> {
                viewModelScope.launch {
                    _authState.value = AuthState.Loading

                    try {
                        val response = authUseCases.registerUser(
                            event.username,
                            event.email,
                            event.password
                        )

                        if (response.isSuccessful) {
                            val data = response.body()
                            val token = data?.token.orEmpty()
                            val refreshToken = data?.refreshToken.orEmpty()
                            val user = data?.user

                            if (response.isSuccessful && user != null) {
                                userPreferences.saveTokens(token, refreshToken)
                                _authState.value = AuthState.Authenticated(user)
                            } else {
                                _authState.value = AuthState.Error("Invalid registration or missing user data")
                            }

                            _authState.value = AuthState.Authenticated(user)
                        } else {
                            _authState.value = AuthState.Error("Registration failed")
                        }
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
                    }
                }
            }

            is AuthEvent.LogoutUser -> {
                viewModelScope.launch {
                    val refreshToken = userPreferences.getRefreshToken()

                    if (!refreshToken.isNullOrEmpty()) {
                        authUseCases.logoutUser(refreshToken)
                    }

                    userPreferences.clearTokens()
                    _authState.value = AuthState.Unauthenticated
                    _logoutCompleted.value = true
                }
            }
        }
    }
}
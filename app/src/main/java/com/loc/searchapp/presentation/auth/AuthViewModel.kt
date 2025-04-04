package com.loc.searchapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.data.preferences.UserPreferences
import com.loc.searchapp.domain.model.User
import com.loc.searchapp.domain.usecases.auth.AuthUseCases
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
    val logoutCompleted: StateFlow<Boolean> = _logoutCompleted

    init {
        viewModelScope.launch {
            userPreferences.user.collect { savedUser ->
                if (savedUser != null) {
                    _authState.value = AuthState.Authenticated(savedUser)
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
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

                        val data = response.body()

                        val fullUser = User(
                            id = data?.user?.id.toString(),
                            username = data?.user?.username.toString(),
                            email = data?.user?.email.toString(),
                            token = data?.token.toString(),
                            refreshToken = data?.refreshToken.toString()
                        )

                        if (response.isSuccessful) {
                            userPreferences.saveUser(fullUser)
                            _authState.value = AuthState.Authenticated(fullUser)
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

                        val user = response.body()?.user

                        if (response.isSuccessful) {
                            userPreferences.saveUser(user)
                            _authState.value = AuthState.Authenticated(user)
                        }
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error(e.localizedMessage ?: "Unknown error")
                    }
                }
            }

            is AuthEvent.LogoutUser -> {
                viewModelScope.launch {
                    userPreferences.clearUser()
                    _authState.value = AuthState.Unauthenticated
                    _logoutCompleted.value = true
                }
            }
        }
    }
}
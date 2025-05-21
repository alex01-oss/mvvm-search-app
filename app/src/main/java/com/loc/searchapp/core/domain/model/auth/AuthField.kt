package com.loc.searchapp.core.domain.model.auth

import androidx.compose.ui.graphics.painter.Painter

data class AuthField(
    val value: String,
    val onValueChange: (String) -> Unit,
    val placeholder: String,
    val icon: Painter,
    val isPassword: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isPasswordVisible: Boolean = false,
    val onPasswordVisibilityChange: (() -> Unit)? = null
)

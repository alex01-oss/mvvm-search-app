package com.loc.searchapp.core.utils

import android.util.Patterns

object FormValidator {
    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty."
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email."
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 6 -> "Password must contain at least 6 characters"
            else -> null
        }
    }

    fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Username cannot be empty."
            username.length < 3 -> "Password must contain at least 3 characters"
            else -> null
        }
    }

    fun validateRequired(value: String, fieldName: String): String? {
        return if (value.isBlank()) "$fieldName cannot be empty." else null
    }
}
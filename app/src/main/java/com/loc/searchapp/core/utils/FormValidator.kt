package com.loc.searchapp.core.utils

import android.util.Patterns

object FormValidator {
    fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Username cannot be empty."
            username.length < 3 -> "Username must contain at least 3 characters"
            else -> null
        }
    }

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty."
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email."
            else -> null
        }
    }

    fun validatePhone(phone: String): String? {
        return when {
            phone.isBlank() -> "Phone number cannot be empty."
            phone.length < 10 -> "Phone number must contain at least 10 digits"
            !phone.matches(Regex("^[+]?[0-9\\s\\-()]+$")) -> "Please enter a valid phone number"
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
}
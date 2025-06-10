package com.loc.searchapp.feature.shared.model

data class FormState(
    val email: String = "",
    val password: String = "",
    val fullname: String = "",
    val phone: String = "",
    val passwordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val fullnameError: String? = null,
    val phoneError: String? = null
)

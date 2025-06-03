package com.loc.searchapp.feature.register.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.auth.AuthField
import com.loc.searchapp.core.utils.FormValidator
import com.loc.searchapp.feature.shared.components.AuthForm
import com.loc.searchapp.feature.shared.model.AuthEvent
import com.loc.searchapp.feature.shared.model.AuthState
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onLoginClick: () -> Unit,
    onAuthenticated: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validateForm(): Boolean {
        usernameError = FormValidator.validateUsername(username)
        emailError = FormValidator.validateEmail(email)
        passwordError = FormValidator.validatePassword(password)

        return usernameError == null && emailError == null && passwordError == null
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onAuthenticated()
        }
    }

    AuthForm(
        modifier = modifier,
        title = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 28.sp, fontWeight = FontWeight.Bold)) {
                append(stringResource(id = R.string.sign_up_main))
            }
            append(" ")
            withStyle(SpanStyle(color = colorResource(id = R.color.light_red), fontSize = 28.sp, fontWeight = FontWeight.Bold)) {
                append(stringResource(id = R.string.sign_up_suffix))
            }
        },
        fields = listOf(
            AuthField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = null
                },
                placeholder = stringResource(id = R.string.full_name),
                icon = painterResource(R.drawable.person),
                isError = usernameError != null,
                errorMessage = usernameError
            ),
            AuthField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                placeholder = stringResource(id = R.string.email),
                icon = painterResource(R.drawable.mail),
                isError = emailError != null,
                errorMessage = emailError
            ),
            AuthField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                placeholder = stringResource(id = R.string.password),
                icon = painterResource(R.drawable.lock),
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
                isError = passwordError != null,
                errorMessage = passwordError
            )
        ),
        onSubmitClick = {
            if (validateForm()) {
                viewModel.onEvent(AuthEvent.LoginUser(email, password))
            }
        },
        isLoading = authState is AuthState.Loading,
        submitButtonText = stringResource(id = R.string.login),
        onBottomTextClick = {
            viewModel.clearError()
            onLoginClick()
        },
        bottomText = stringResource(id = R.string.not_have_account),
        showError = (authState as? AuthState.Error)?.message
    )
}
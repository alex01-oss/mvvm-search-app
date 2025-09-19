package com.loc.searchapp.presentation.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.loc.searchapp.core.ui.values.Dimens.LogoHeight
import com.loc.searchapp.core.utils.FormValidator
import com.loc.searchapp.presentation.shared.components.AuthForm
import com.loc.searchapp.presentation.shared.model.AuthEvent
import com.loc.searchapp.presentation.shared.model.AuthState
import com.loc.searchapp.presentation.shared.network.NetworkObserver
import com.loc.searchapp.presentation.shared.network.NetworkStatus
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onRegisterClick: () -> Unit,
    onAuthenticated: () -> Unit,
) {
    val authState by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val networkObserver = remember { NetworkObserver(context) }
    val networkStatus by networkObserver.networkStatus.collectAsState(
        initial = NetworkStatus.Available
    )

    val networkError = stringResource(R.string.no_internet_connection)

    fun validateForm(): Boolean {
        emailError = FormValidator.validateEmail(email)
        passwordError = FormValidator.validatePassword(password)

        return emailError == null && passwordError == null
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onAuthenticated()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(LogoHeight))

        Image(
            modifier = Modifier.height(LogoHeight),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo),
            contentScale = ContentScale.Fit,
        )

        AuthForm(
            modifier = Modifier.fillMaxWidth(),
            title = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(id = R.string.sign_in_main))
                }
                append(" ")
                withStyle(
                    SpanStyle(
                        color = colorResource(id = R.color.light_red),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(id = R.string.sign_in_suffix))
                }
            },
            fields = listOf(
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
                    if (networkStatus != NetworkStatus.Available) {
                        viewModel.setError(networkError)
                    } else {
                        viewModel.onEvent(AuthEvent.LoginUser(email, password))
                    }
                }
            },
            isLoading = authState is AuthState.Loading,
            submitButtonText = stringResource(id = R.string.login),
            onBottomTextClick = {
                viewModel.clearError()
                onRegisterClick()
            },
            bottomContent = {
                Text(
                    text = stringResource(id = R.string.not_have_account),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            showError = (authState as? AuthState.Error)?.message
        )
    }
}
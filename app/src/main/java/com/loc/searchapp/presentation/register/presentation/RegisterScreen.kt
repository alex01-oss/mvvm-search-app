package com.loc.searchapp.presentation.register.presentation

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
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onLoginClick: () -> Unit,
    onAuthenticated: () -> Unit,
) {
    val authState by viewModel.authState.collectAsState()

    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var fullnameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val networkObserver = remember { NetworkObserver(context) }
    val networkStatus by networkObserver.networkStatus.collectAsState(
        initial = NetworkStatus.Available
    )

    val networkError = stringResource(R.string.no_internet_connection)

    fun validateForm(): Boolean {
        fullnameError = FormValidator.validateUsername(fullname)
        emailError = FormValidator.validateEmail(email)
        phoneError = FormValidator.validatePhone(phone)
        passwordError = FormValidator.validatePassword(password)

        return fullnameError == null && emailError == null && phoneError == null && passwordError == null
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
                    append(stringResource(id = R.string.sign_up_main))
                }
                append(" ")
                withStyle(
                    SpanStyle(
                        color = colorResource(id = R.color.light_red),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(id = R.string.sign_up_suffix))
                }
            },
            fields = listOf(
                AuthField(
                    value = fullname,
                    onValueChange = {
                        fullname = it
                        fullnameError = null
                    },
                    placeholder = stringResource(id = R.string.full_name),
                    icon = painterResource(R.drawable.person),
                    isError = fullnameError != null,
                    errorMessage = fullnameError
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
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = null
                    },
                    placeholder = stringResource(id = R.string.phone),
                    icon = painterResource(R.drawable.phone),
                    isError = phoneError != null,
                    errorMessage = phoneError
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
                        viewModel.onEvent(AuthEvent.RegisterUser(fullname, email, phone, password))
                    }
                }
            },
            isLoading = authState is AuthState.Loading,
            submitButtonText = stringResource(id = R.string.register),
            onBottomTextClick = {
                viewModel.clearError()
                onLoginClick()
            },
            bottomContent = {
                Text(
                    text = stringResource(id = R.string.have_account),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            showError = (authState as? AuthState.Error)?.message
        )
    }
}

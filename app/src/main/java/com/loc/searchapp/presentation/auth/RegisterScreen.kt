package com.loc.searchapp.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.loc.searchapp.R
import com.loc.searchapp.presentation.Dimens.ExtraSmallPadding
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.presentation.auth.components.CustomTextField
import com.loc.searchapp.presentation.auth.components.PasswordTextField
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.components.InlineErrorMessage
import com.loc.searchapp.presentation.nvgraph.Route
import com.loc.searchapp.utils.FormValidator

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    navController: NavController,
    onLoginClick: () -> Unit
) {
    val authState = viewModel.authState.collectAsState().value

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            navController.navigate(Route.HomeScreen.route) {
                popUpTo(Route.RegisterScreen.route) { inclusive = true }
            }
        }
    }

    fun validateForm(): Boolean {
        usernameError = FormValidator.validateUsername(username)
        emailError = FormValidator.validateEmail(email)
        passwordError = FormValidator.validatePassword(password)

        return usernameError == null && emailError == null && passwordError == null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(135.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Sign ")  // translate
                }
                withStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.light_red),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("UP") // translate
                }
            }
        )

        Spacer(modifier.height(MediumPadding1))

        if (authState is AuthState.Error) {
            InlineErrorMessage(message = authState.message)
        }

        Column(modifier.fillMaxWidth()) {
            CustomTextField(
                value = username,
                placeholder = stringResource(id = R.string.full_name),
                onValueChange = {
                    username = it
                    if (usernameError != null) usernameError = null
                },
                painterResource = painterResource(R.drawable.person),
                isError = usernameError != null
            )

            if (usernameError != null) {
                Text(
                    text = usernameError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier.padding(start = SmallPadding, top = 4.dp)
                )
            }
        }

        Spacer(modifier.height(SmallPadding))

        Column(modifier.fillMaxWidth()) {
            CustomTextField(
                value = email,
                placeholder = stringResource(id = R.string.email),
                onValueChange = {
                    email = it
                    if (emailError != null) emailError = null
                },
                painterResource = painterResource(R.drawable.mail),
                isError = emailError != null
            )

            if (emailError != null) {
                Text(
                    text = emailError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier.padding(start = SmallPadding, top = 4.dp)
                )
            }
        }

        Spacer(modifier.height(SmallPadding))

        Column(modifier.fillMaxWidth()) {
            PasswordTextField(
                value = password,
                placeholder = stringResource(id = R.string.password),
                onValueChange = {
                    password = it
                    if (passwordError != null) passwordError = null
                },
                painterResource = painterResource(R.drawable.lock),
                isPasswordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it },
                isError = passwordError != null
            )

            if (passwordError != null) {
                Text(
                    text = passwordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier.padding(start = SmallPadding, top = 4.dp)
                )
            }
        }

        Spacer(modifier.height(MediumPadding1))

        Button(
            onClick = {
                if (validateForm()) {
                    viewModel.onEvent(AuthEvent.RegisterUser(username, email, password))
                }
            },
            modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier.height(MediumPadding1))

        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.or),
                modifier.padding(horizontal = SmallPadding),
                style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            HorizontalDivider(modifier.weight(1f))
        }

        Spacer(modifier.height(ExtraSmallPadding))

        TextButton(onClick = {
            viewModel.clearError()
            onLoginClick()
        }) {
            Text(
                text = stringResource(id = R.string.have_account),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
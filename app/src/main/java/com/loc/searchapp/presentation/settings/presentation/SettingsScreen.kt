package com.loc.searchapp.presentation.settings.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.loc.searchapp.core.data.remote.dto.User
import com.loc.searchapp.core.domain.model.auth.AuthField
import com.loc.searchapp.presentation.shared.components.notifications.AppDialog
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.presentation.shared.components.AuthForm
import com.loc.searchapp.presentation.shared.model.AuthEvent
import com.loc.searchapp.presentation.shared.model.AuthState
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val state by viewModel.authState.collectAsState()
    val context = LocalContext.current

    var fullname by rememberSaveable { mutableStateOf(state.getUser()?.fullname.orEmpty()) }
    var email by rememberSaveable { mutableStateOf(state.getUser()?.email.orEmpty()) }
    var phone by rememberSaveable { mutableStateOf(state.getUser()?.phone.orEmpty()) }
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var showSaveDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val emptyOldPasswordMessage = stringResource(id = R.string.empty_old_password)

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.profile_settings),
                onBackClick = onBackClick,
                showBackButton = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            val fields = listOf(
                AuthField(
                    value = fullname,
                    onValueChange = { fullname = it },
                    placeholder = stringResource(id = R.string.full_name),
                    icon = painterResource(id = R.drawable.person)
                ),
                AuthField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    icon = painterResource(id = R.drawable.mail)
                ),
                AuthField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = stringResource(id = R.string.phone),
                    icon = painterResource(id = R.drawable.phone)
                ),
                AuthField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    placeholder = stringResource(id = R.string.old_password),
                    icon = painterResource(id = R.drawable.lock),
                    isPassword = true,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
                ),
                AuthField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = stringResource(id = R.string.new_password),
                    icon = painterResource(id = R.drawable.lock),
                    isPassword = true,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
                )
            )

            AuthForm(
                title = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(stringResource(id = R.string.edit_profile))
                    }
                },
                fields = fields,
                isLoading = false,
                submitButtonText = stringResource(id = R.string.save_changes),
                onSubmitClick = {
                    if (oldPassword.isBlank() && newPassword.isNotBlank()) {
                        Toast.makeText(
                            context,
                            emptyOldPasswordMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@AuthForm
                    }

                    viewModel.onEvent(
                        AuthEvent.UpdateUser(
                            fullname = fullname,
                            email = email,
                            phone = phone,
                            currentPassword = oldPassword.takeIf { it.isNotBlank() },
                            newPassword = newPassword.takeIf { it.isNotBlank() }
                        )
                    )
                },
                onBottomTextClick = { showDeleteDialog = true },
                bottomContent = {
                    Text(
                        text = stringResource(id = R.string.delete_account),
                        color = colorResource(id = R.color.light_red)
                    )
                }
            )
        }

        if (showSaveDialog) {
            AppDialog(
                title = stringResource(id = R.string.save_changes),
                message = stringResource(id = R.string.apply_changes),
                confirmLabel = stringResource(id = R.string.yes),
                onConfirm = { showSaveDialog = false },
                dismissLabel = stringResource(id = R.string.cancel),
                onDismiss = { showSaveDialog = false }
            )
        }

        if (showDeleteDialog) {
            AppDialog(
                title = stringResource(id = R.string.delete_account),
                message = stringResource(id = R.string.confirm_delete),
                confirmLabel = stringResource(id = R.string.yes),
                onConfirm = {
                    viewModel.onEvent(AuthEvent.DeleteUser)
                    showDeleteDialog = false
                    onDeleteClick()
                },
                dismissLabel = stringResource(id = R.string.cancel),
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}

fun AuthState.getUser(): User? {
    return (this as? AuthState.Authenticated)?.user
}

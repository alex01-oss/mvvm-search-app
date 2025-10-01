package com.loc.searchapp.presentation.settings.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.auth.User
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.BorderStroke
import com.loc.searchapp.core.ui.values.Dimens.InputFieldHeight
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding2
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.presentation.shared.components.CustomTextField
import com.loc.searchapp.presentation.shared.components.PasswordTextField
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.presentation.shared.components.notifications.AppDialog
import com.loc.searchapp.presentation.shared.model.AuthEvent
import com.loc.searchapp.presentation.shared.model.AuthState
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onUpdated: () -> Unit
) {
    val state by viewModel.authState.collectAsState()
    val isLoading = state is AuthState.Loading

    var fullname by rememberSaveable { mutableStateOf(state.getUser()?.fullname.orEmpty()) }
    var email by rememberSaveable { mutableStateOf(state.getUser()?.email.orEmpty()) }
    var phone by rememberSaveable { mutableStateOf(state.getUser()?.phone.orEmpty()) }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutAllDialog by remember { mutableStateOf(false) }

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
                .padding(paddingValues)
                .padding(horizontal = MediumPadding2)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.edit_profile),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(BasePadding))

            CustomTextField(
                value = fullname,
                placeholder = stringResource(id = R.string.full_name),
                onValueChange = { fullname = it },
                painterResource = painterResource(id = R.drawable.person)
            )
            Spacer(modifier = Modifier.height(BasePadding))

            CustomTextField(
                value = email,
                placeholder = "Email",
                onValueChange = { email = it },
                painterResource = painterResource(id = R.drawable.mail)
            )
            Spacer(modifier = Modifier.height(BasePadding))

            CustomTextField(
                value = phone,
                placeholder = stringResource(id = R.string.phone),
                onValueChange = { phone = it },
                painterResource = painterResource(id = R.drawable.phone)
            )
            Spacer(modifier = Modifier.height(BasePadding))

            PasswordTextField(
                value = newPassword,
                placeholder = stringResource(id = R.string.new_password),
                onValueChange = { newPassword = it },
                painterResource = painterResource(id = R.drawable.lock),
                isPasswordVisible = isPasswordVisible,
                onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
            )
            Spacer(modifier = Modifier.height(MediumPadding2))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(InputFieldHeight),
                onClick = {
                    viewModel.onEvent(
                        AuthEvent.UpdateUser(
                            fullname = fullname,
                            email = email,
                            phone = phone,
                            password = newPassword.takeIf { it.isNotBlank() }
                        )
                    )
                    onUpdated()
                },
                shape = RoundedCornerShape(StrongCorner),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(BasePadding)
                            .clearAndSetSemantics { },
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = BorderStroke
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.save_changes),
                        color = themeAdaptiveColor()
                    )
                }
            }

            Spacer(modifier = Modifier.height(MediumPadding2))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .clearAndSetSemantics { }
                )
                Text(
                    modifier = Modifier.padding(horizontal = BasePadding),
                    text = stringResource(id = R.string.danger_zone),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .clearAndSetSemantics { }
                )
            }

            Spacer(modifier = Modifier.height(BasePadding))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BasePadding)
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(InputFieldHeight),
                    onClick = { showLogoutAllDialog = true },
                    shape = RoundedCornerShape(StrongCorner),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = stringResource(id = R.string.logout_all),
                        fontSize = 14.sp
                    )
                }

                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(InputFieldHeight),
                    onClick = { showDeleteDialog = true },
                    shape = RoundedCornerShape(StrongCorner),
                    border = BorderStroke(1.dp, colorResource(id = R.color.light_red)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = R.color.light_red)
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.delete_account),
                        fontSize = 14.sp
                    )
                }
            }
        }

        if (showLogoutAllDialog) {
            AppDialog(
                title = stringResource(id = R.string.logout_all_devices),
                message = stringResource(id = R.string.confirm_logout_all),
                confirmLabel = stringResource(id = R.string.yes),
                onConfirm = {
                    viewModel.onEvent(AuthEvent.LogoutAllDevices)
                    showLogoutAllDialog = false
                },
                dismissLabel = stringResource(id = R.string.cancel),
                onDismiss = { showLogoutAllDialog = false }
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
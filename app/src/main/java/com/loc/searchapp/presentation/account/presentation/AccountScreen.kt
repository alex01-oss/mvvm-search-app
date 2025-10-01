package com.loc.searchapp.presentation.account.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.presentation.shared.components.Avatar
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.core.ui.values.Dimens.AvatarHeight
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.presentation.account.components.AccountOption
import com.loc.searchapp.presentation.shared.components.notifications.GuestUser
import com.loc.searchapp.presentation.shared.model.AuthEvent
import com.loc.searchapp.presentation.shared.model.AuthState
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    onAuthClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onAddPostClick: () -> Unit,
    onAboutClick: () -> Unit,
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val authState = viewModel.authState.collectAsState().value
    val themeColor = themeAdaptiveColor()

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.account),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null,
                        modifier = Modifier.size(IconSize)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding() + BasePadding)
                .padding(horizontal = BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (authState) {
                is AuthState.Authenticated -> {
                    val user = authState.user

                    Spacer(modifier = Modifier.height(BasePadding))

                    Avatar(
                        firstName = user?.fullname?.substringBefore(" ").toString(),
                        lastName = user?.fullname?.substringAfter(" ").toString(),
                        size = AvatarHeight,
                        textStyle = MaterialTheme.typography.titleLarge,
                    )

                    Spacer(modifier = Modifier.height(BasePadding))

                    Text(
                        text = user?.fullname.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = user?.email.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    if (user?.role == "admin") {
                        Text(
                            text = "role: admin",
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(vertical = BasePadding),
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.height(BasePadding))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        AccountOption(
                            icon = Icons.Default.Settings,
                            text = stringResource(id = R.string.settings),
                            onClick = onSettingsClick
                        )
                        AccountOption(
                            icon = Icons.Default.Language,
                            text = stringResource(id = R.string.change_language),
                            onClick = onLanguageClick
                        )
                        AccountOption(
                            icon = Icons.Default.Info,
                            text = stringResource(id = R.string.about_us),
                            onClick = onAboutClick
                        )
                        AccountOption(
                            icon = Icons.Default.AdminPanelSettings,
                            text = stringResource(id = R.string.our_blog),
                            onClick = onAddPostClick
                        )
                    }

                    Spacer(modifier = Modifier.height(BasePadding))

                    Button(
                        onClick = {
                            viewModel.onEvent(AuthEvent.LogoutUser)
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(BasePadding),
                    ) {
                        Text(
                            text = stringResource(id = R.string.logout),
                            color = themeColor,
                        )
                    }
                }

                is AuthState.Unauthenticated -> {
                    GuestUser(onAuthClick = onAuthClick)
                }

                is AuthState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is AuthState.Error -> {
                    Text(stringResource(id = R.string.error, authState.message))
                }
            }
        }
    }
}
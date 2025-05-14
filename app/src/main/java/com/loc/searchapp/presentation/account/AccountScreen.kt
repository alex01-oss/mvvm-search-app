package com.loc.searchapp.presentation.account

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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.presentation.Dimens.AvatarHeight
import com.loc.searchapp.presentation.Dimens.IconSize
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.presentation.account.components.AccountOption
import com.loc.searchapp.presentation.auth.AuthEvent
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.auth.components.GuestUser
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.base.ProductViewModel
import com.loc.searchapp.presentation.common.components.Avatar
import com.loc.searchapp.presentation.common.components.SharedTopBar

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    onAuthClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onAddPostClick: () -> Unit,
    viewModel: AuthViewModel,
    productViewModel: ProductViewModel
) {
    val authState = viewModel.authState.collectAsState().value

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.account),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null,
                        modifier.size(IconSize)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding() + MediumPadding1)
                .padding(horizontal = MediumPadding1),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (authState) {
                is AuthState.Authenticated -> {
                    val user = authState.user

                    Spacer(modifier = Modifier.height(MediumPadding1))

                    Avatar(
                        firstName = user?.username?.substringBefore(" ").toString(),
                        lastName = user?.username?.substringAfter(" ").toString(),
                        size = AvatarHeight,
                        textStyle = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier.height(SmallPadding))

                    Text(
                        text = user?.username.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = user?.email.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier.height(MediumPadding1))

                    Column(modifier.fillMaxWidth()) {
                        AccountOption(
                            icon = Icons.Default.Settings,
                            text = stringResource(id = R.string.settings)
                        )
                        AccountOption(
                            icon = Icons.Default.Lock,
                            text = stringResource(id = R.string.change_password)
                        )
                        AccountOption(
                            icon = Icons.Default.Language,
                            text = stringResource(id = R.string.change_language),
                            onClick = onLanguageClick
                        )
                        AccountOption(
                            icon = Icons.Default.AdminPanelSettings,
                            text = stringResource(id = R.string.add_post),
                            onClick = onAddPostClick
                        )
                    }

                    Spacer(modifier.height(MediumPadding1))

                    Button(
                        onClick = {
                            viewModel.onEvent(AuthEvent.LogoutUser)
                            productViewModel.clearLocalCart()
                        },
                        modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.logout),
                            color = Color.White
                        )
                    }
                }

                is AuthState.Unauthenticated -> {
                    GuestUser(onAuthClick = onAuthClick)
                }

                is AuthState.Loading -> {
                    Box(
                        modifier.fillMaxSize(),
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

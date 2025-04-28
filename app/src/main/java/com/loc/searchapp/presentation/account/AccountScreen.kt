package com.loc.searchapp.presentation.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.presentation.Dimens.AvatarHeight
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.presentation.account.components.AccountOption
import com.loc.searchapp.presentation.auth.AuthEvent
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.auth.components.GuestUser
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.base.ProductViewModel
import com.loc.searchapp.presentation.common.components.Avatar

@Composable
fun AccountScreen(
    onAuthClick: () -> Unit,
    onLanguageClick: () -> Unit,
    viewModel: AuthViewModel,
    productViewModel: ProductViewModel
) {
    val authState = viewModel.authState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            )
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when (authState) {
            is AuthState.Authenticated -> {
                val user = authState.user

                Avatar(
                    firstName = user?.username?.substringBefore(" ").toString(),
                    lastName = user?.username?.substringAfter(" ").toString(),
                    size = AvatarHeight,
                    textStyle = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(SmallPadding))

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

                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = stringResource(id = R.string.cart_count),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(MediumPadding1))

                Column(modifier = Modifier.fillMaxWidth()) {
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
                        icon = Icons.Default.Info,
                        text = stringResource(id = R.string.help)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.onEvent(AuthEvent.LogoutUser(""))
                        productViewModel.clearLocalCart()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
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

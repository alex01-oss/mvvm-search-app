package com.loc.searchapp.presentation.account

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loc.searchapp.R
import com.loc.searchapp.presentation.Dimens.AvatarHeight
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.presentation.auth.AuthEvent
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.auth.AuthViewModel
import com.loc.searchapp.presentation.common.Avatar

@Composable
fun AccountScreen(
    onAuthClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState = viewModel.authState.collectAsState().value
    val logoutCompleted = viewModel.logoutCompleted.collectAsState().value

    LaunchedEffect(logoutCompleted) {
        if (logoutCompleted) {
            onLogoutClick()
        }
    }

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
        if (authState is AuthState.Authenticated) {
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
                text = "Cart: empty",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(MediumPadding1))

            Column(modifier = Modifier.fillMaxWidth()) {
                AccountOption(icon = Icons.Default.Settings, text = "Settings")
                AccountOption(icon = Icons.Default.Lock, text = "Change password")
                AccountOption(icon = Icons.Default.Info, text = "Help")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.onEvent(AuthEvent.LogoutUser())
                    onLogoutClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Log out", color = Color.White)
            }

        } else {
            Avatar(
                firstName = "",
                lastName = "",
                size = AvatarHeight,
                textStyle = MaterialTheme.typography.labelLarge,
                placeholder = {
                    Image(
                        painter = painterResource(R.drawable.person),
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(40.dp)
                    )
                }
            )

            Text(text = "To be able to create a cart and place an order, you must log in or create an account.")

            Spacer(modifier = Modifier.height(MediumPadding1))

            Button(
                onClick = onAuthClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Auth", color = Color.White)
            }
        }
    }
}

@Composable
fun AccountOption(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SmallPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            tint = Color.Gray,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(SmallPadding))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.auth.AuthViewModel
import com.loc.searchapp.presentation.common.Avatar

@Composable
fun HomeTopBar(
    viewModel: AuthViewModel
) {
    val authState = viewModel.authState.collectAsState().value

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .height(60.dp)
        )

        when (authState) {
            is AuthState.Authenticated -> {
                val user = authState.user

                Avatar(
                    firstName = user?.username?.substringBefore(" ") ?: "Guest",
                    lastName = user?.username?.substringAfter(" ") ?: "Guest"
                )
            }
            is AuthState.Unauthenticated -> {
                Avatar(
                    firstName = "",
                    lastName = "",
                    placeholder = {
                        Image(
                            painter = painterResource(R.drawable.person),
                            contentDescription = "User Avatar",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                )
            }
            is AuthState.Loading -> {}
            is AuthState.Error -> {}
        }
    }
}
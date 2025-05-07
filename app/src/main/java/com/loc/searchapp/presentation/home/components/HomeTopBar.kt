package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.components.Avatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel
) {
    val authState = viewModel.authState.collectAsState().value

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = MediumPadding1),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        id =
                            if (isSystemInDarkTheme()) R.drawable.ic_logo_dark
                            else R.drawable.ic_logo_light
                    ),
                    contentDescription = null,
                    modifier.width(120.dp).height(48.dp)
                )

                when (authState) {
                    is AuthState.Authenticated -> {
                        val user = authState.user

                        Avatar(
                            firstName = user?.username?.substringBefore(" ")
                                ?: stringResource(id = R.string.guest),
                            lastName = user?.username?.substringAfter(" ")
                                ?: stringResource(id = R.string.guest)
                        )
                    }

                    is AuthState.Unauthenticated -> {
                        Avatar(firstName = "", lastName = "")
                    }

                    is AuthState.Loading -> {}
                    is AuthState.Error -> {}
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
package com.loc.searchapp.feature.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.components.common.Avatar
import com.loc.searchapp.core.ui.values.Dimens.AvatarSize
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.TopBarPadding
import com.loc.searchapp.core.ui.values.Dimens.TopLogoHeight
import com.loc.searchapp.feature.shared.model.AuthState
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    scrollState: Float = 0f,
    logoAlpha: Float
) {
    val authState = viewModel.authState.collectAsState().value

    val backgroundColor by animateColorAsState(
        targetValue = lerp(
            Color.Transparent,
            MaterialTheme.colorScheme.primary,
            scrollState.coerceIn(0f, 1f)
        ),
        animationSpec = tween(300),
        label = "AppBarColor"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor
    ) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = TopBarPadding, end = MediumPadding1),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(AvatarSize))

                    Image(
                        painter = painterResource(id = R.drawable.logo_white),
                        contentDescription = null,
                        modifier = Modifier
                            .height(TopLogoHeight)
                            .graphicsLayer {
                                alpha = logoAlpha
                            }
                    )

                    when (authState) {
                        is AuthState.Authenticated -> {
                            val user = authState.user

                            Avatar(
                                firstName = user?.fullname?.substringBefore(" ")
                                    ?: stringResource(id = R.string.guest),
                                lastName = user?.fullname?.substringAfter(" ")
                                    ?: stringResource(id = R.string.guest),
                            )
                        }

                        is AuthState.Unauthenticated -> {
                            Avatar(
                                firstName = "",
                                lastName = "",
                            )
                        }

                        else -> {
                            Spacer(modifier = Modifier.width(AvatarSize))
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

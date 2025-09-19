package com.loc.searchapp.presentation.shared.components

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.EmptyIconSize
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import java.net.ConnectException
import java.net.SocketTimeoutException

@Composable
fun EmptyScreen(
    message: String? = null,
    iconId: Int = R.drawable.ic_network_error,
    error: Throwable? = null
) {
    val context = LocalContext.current

    val finalMessage = remember(message, error, context) {
        message ?: error?.let { parseErrorMessage(it, context) }
        ?: context.getString(R.string.unknown_state)
    }

    val finalIcon = remember(message) {
        if (message != null) iconId else R.drawable.ic_network_error
    }

    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 0.3f else 0f,
        animationSpec = tween(durationMillis = 1500), label = ""
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    EmptyContent(
        alphaAnim = alphaAnimation,
        message = finalMessage,
        iconId = finalIcon
    )
}

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    alphaAnim: Float,
    message: String,
    iconId: Int
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(EmptyIconSize).alpha(alphaAnim),
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = R.string.empty_icon),
            tint = if (isSystemInDarkTheme()) LightGray else DarkGray
        )
        Text(
            modifier = Modifier.padding(SmallPadding).alpha(alphaAnim),
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSystemInDarkTheme()) LightGray else DarkGray,
        )
    }
}

fun parseErrorMessage(error: Throwable, context: Context): String {
    return when (error) {
        is SocketTimeoutException -> context.getString(R.string.server_unavailable)
        is ConnectException -> context.getString(R.string.internet_unavailable)
        else -> context.getString(R.string.unknown_state)
    }
}
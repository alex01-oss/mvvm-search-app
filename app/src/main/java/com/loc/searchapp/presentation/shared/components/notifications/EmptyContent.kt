package com.loc.searchapp.presentation.shared.components.notifications

import android.content.Context
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.EmptyIconSize
import java.net.ConnectException
import java.net.SocketTimeoutException

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    message: String,
    iconId: Int
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(EmptyIconSize)
                .alpha(1f)
                .clearAndSetSemantics { },
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) LightGray else DarkGray
        )
        Text(
            modifier = Modifier.padding(BasePadding).alpha(1f),
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
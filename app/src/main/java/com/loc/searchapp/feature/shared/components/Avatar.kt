package com.loc.searchapp.feature.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.AvatarSize
import com.loc.searchapp.core.ui.values.Dimens.BorderStroke

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    firstName: String?,
    lastName: String?,
    size: Dp = AvatarSize,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    placeholder: @Composable (() -> Unit)? = null,
    onAvatarClick: () -> Unit
) {
    Box(
        modifier
            .clickable(onClick = onAvatarClick)
            .size(size)
            .clip(CircleShape)
            .border(
                width = BorderStroke,
                color = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        val hasName = !firstName.isNullOrBlank() && !lastName.isNullOrBlank()

        if (hasName) {
            val initials = (firstName.take(1) + lastName.take(1)).uppercase()
            Text(text = initials, style = textStyle, color = Color.White)
        } else {
            placeholder?.invoke() ?: DefaultPlaceholder(size = size)
        }
    }
}

@Composable
fun DefaultPlaceholder(
    modifier: Modifier = Modifier,
    size: Dp
) {
    Box(
        modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.person),
            contentDescription = stringResource(id = R.string.avatar),
            modifier = Modifier.size(size * 0.55f)
        )
    }
}
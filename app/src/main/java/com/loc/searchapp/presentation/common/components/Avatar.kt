package com.loc.searchapp.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R

@Composable
fun Avatar(
    firstName: String?,
    lastName: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    placeholder: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        val hasName = !firstName.isNullOrBlank() && !lastName.isNullOrBlank()

        if (hasName) {
            val initials = (firstName.take(1) + lastName.take(1)).uppercase()
            Text(text = initials, style = textStyle, color = Color.White)
        } else {
            placeholder?.invoke() ?: DefaultPlaceholder(size)
        }
    }
}

@Composable
fun DefaultPlaceholder(size: Dp) {
    Image(
        painter = painterResource(id = R.drawable.avatar),
        contentDescription = null,
        modifier = Modifier.size(size).clip(CircleShape)
    )
}

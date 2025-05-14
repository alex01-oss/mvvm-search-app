package com.loc.searchapp.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
    modifier: Modifier = Modifier,
    firstName: String?,
    lastName: String?,
    size: Dp = 40.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    placeholder: @Composable (() -> Unit)? = null
) {
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .border(
                width = 2.dp,
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
            placeholder?.invoke() ?: DefaultPlaceholder(size)
        }
    }
}

@Composable
fun DefaultPlaceholder(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.person),
            contentDescription = null,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}
package com.loc.searchapp.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.loc.searchapp.utils.toHslColor

@Composable
fun Avatar(
    firstName: String?,
    lastName: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    placeholder: @Composable (() -> Unit)? = null
) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        val hasName = !firstName.isNullOrBlank() && !lastName.isNullOrBlank()

        if (hasName) {
            val color = remember(firstName, lastName) {
                val name = listOf(firstName, lastName)
                    .joinToString(separator = "")
                    .uppercase()
                Color(name.toHslColor())
            }
            val initials = (firstName.take(1) + lastName.take(1)).uppercase()

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(SolidColor(color))
            }
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
            .background(Color.Gray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Default Avatar",
            tint = Color.White,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

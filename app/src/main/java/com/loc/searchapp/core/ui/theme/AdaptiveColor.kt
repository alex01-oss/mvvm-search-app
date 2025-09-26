package com.loc.searchapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun themeAdaptiveColor(): Color {
    return if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onBackground
    } else {
        Color.White
    }
}

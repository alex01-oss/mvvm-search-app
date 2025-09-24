package com.loc.searchapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundRes = if (isDarkTheme) R.drawable.bg else R.drawable.bg_light

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(backgroundRes),
            contentDescription = stringResource(R.string.background),
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        content()
    }
}

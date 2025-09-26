package com.loc.searchapp.presentation.shared.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.core.ui.values.Dimens.BasePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTopBar(
    title: String? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    val themeColor = themeAdaptiveColor()

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    CompositionLocalProvider(LocalContentColor provides themeColor) {
                        leadingIcon()
                    }
                }
                Spacer(modifier = Modifier.width(BasePadding))
                if (title != null) Text(title, color = themeColor)
            }
        },
        navigationIcon = {
            if (showBackButton && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        tint = themeColor,
                        contentDescription = stringResource(id = R.string.arrow_back)
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            actionIconContentColor = themeColor,
            navigationIconContentColor = themeColor
        )
    )
}
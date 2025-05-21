package com.loc.searchapp.feature.home.components

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import com.loc.searchapp.core.ui.values.Dimens.IconSize2

@Composable
fun SocialIcon(
    @DrawableRes iconRes: Int,
    link: String,
) {
    val context = LocalContext.current
    IconButton(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, link.toUri())
            context.startActivity(intent)
        }
    ) {
        Icon(
            modifier = Modifier.size(IconSize2),
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
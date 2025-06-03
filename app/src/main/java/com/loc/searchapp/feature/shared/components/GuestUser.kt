package com.loc.searchapp.feature.shared.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.AvatarHeight
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.components.common.Avatar

@Composable
fun GuestUser(
    onAuthClick: () -> Unit
) {
    Avatar(
        firstName = "",
        lastName = "",
        size = AvatarHeight,
        textStyle = MaterialTheme.typography.labelLarge,
    )

    Spacer(modifier = Modifier.height(MediumPadding1))

    Text(
        text = stringResource(id = R.string.must_login),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(MediumPadding1))

    Button(
        onClick = onAuthClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(id = R.string.auth), color = Color.White)
    }
}
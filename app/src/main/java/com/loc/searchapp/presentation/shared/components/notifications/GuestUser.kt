package com.loc.searchapp.presentation.shared.components.notifications

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
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.presentation.shared.components.Avatar

@Composable
fun GuestUser(
    onAuthClick: () -> Unit
) {
    Avatar(
        firstName = "",
        lastName = "",
        size = AvatarHeight,
        textStyle = MaterialTheme.typography.labelLarge,
        onAvatarClick = {}
    )

    Spacer(modifier = Modifier.height(BasePadding))

    Text(
        text = stringResource(id = R.string.must_login),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(BasePadding))

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
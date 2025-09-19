package com.loc.searchapp.presentation.about.presentation

import android.content.Intent
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import com.loc.searchapp.BuildConfig
import com.loc.searchapp.R
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.core.ui.values.Dimens.AboutLogoSize
import com.loc.searchapp.core.ui.values.Dimens.AboutTextWidth
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.presentation.about.components.ExpandableItem

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val year = remember { Calendar.getInstance().get(Calendar.YEAR) }

    Scaffold(
        modifier = modifier,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.about),
                onBackClick = onBackClick,
                showBackButton = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MediumPadding1),
            verticalArrangement = Arrangement.spacedBy(BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MediumPadding1))

            Icon(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(AboutLogoSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .scale(0.8f)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = stringResource(id = R.string.version, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = stringResource(id = R.string.about_company, "© $year"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(AboutTextWidth)
            )

            HorizontalDivider(Modifier.padding(vertical = BasePadding))

            ExpandableItem(
                title = stringResource(id = R.string.privacy_policy),
                content = stringResource(id = R.string.privacy_policy_text)
            )

            ExpandableItem(
                title = stringResource(id = R.string.terms_of_use),
                content = stringResource(id = R.string.terms_of_use_text)
            )

            Spacer(modifier = Modifier.height(BasePadding))

            Text(
                text = stringResource(id = R.string.support),
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = stringResource(id = R.string.support_email),
                color = colorResource(id = R.color.light_red),
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:sale@pdtools.shop".toUri()
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}
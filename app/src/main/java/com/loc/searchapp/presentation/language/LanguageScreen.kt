package com.loc.searchapp.presentation.language

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.presentation.Dimens.BasePadding
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.presentation.common.components.AppDialog
import com.loc.searchapp.presentation.common.components.SharedTopBar
import com.loc.searchapp.presentation.language.components.LanguageOption
import com.loc.searchapp.utils.LanguagePreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LanguageScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var selectedLanguage by remember { mutableStateOf(LanguagePreference.getLanguage(context)) }
    var showRestartDialog by remember { mutableStateOf(false) }
    var pendingLanguage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        onBackClick()
    }

    Column(
        modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        SharedTopBar(
            title = stringResource(id = R.string.language_settings),
            onBackClick = onBackClick,
            showBackButton = true
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        Text(
            modifier = Modifier.padding(horizontal = MediumPadding1),
            text = stringResource(id = R.string.select_language),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(BasePadding))

        LanguageOption(
            language = stringResource(id = R.string.system_language),
            isSelected = selectedLanguage == "system",
            onClick = {
                pendingLanguage = "system"
                showRestartDialog = true
            },
            id = R.drawable.globe
        )

        Spacer(modifier = Modifier.height(SmallPadding))

        LanguageOption(
            language = "English",
            isSelected = selectedLanguage == "en",
            onClick = {
                pendingLanguage = "en"
                showRestartDialog = true
            },
            id = R.drawable.flag_uk
        )

        Spacer(modifier = Modifier.height(SmallPadding))

        LanguageOption(
            language = "Українська",
            isSelected = selectedLanguage == "uk",
            onClick = {
                pendingLanguage = "uk"
                showRestartDialog = true
            },
            id = R.drawable.flag_ukraine
        )
    }

    if (showRestartDialog) {
        AppDialog(
            title = stringResource(id = R.string.change_language),
            message = stringResource(id = R.string.restart_app_message),
            confirmLabel = stringResource(id = R.string.yes),
            onConfirm = {
                showRestartDialog = false
                LanguagePreference.setLanguage(context, pendingLanguage)
                selectedLanguage = pendingLanguage

                coroutineScope.launch {
                    delay(300)
                    activity?.recreate()
                }
            },
            dismissLabel = stringResource(id = R.string.cancel),
            onDismiss = { showRestartDialog = false }
        )
    }
}
package com.loc.searchapp.presentation.language.presentation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import com.loc.searchapp.R
import com.loc.searchapp.presentation.shared.components.notifications.AppDialog
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.utils.LanguagePreference
import com.loc.searchapp.presentation.language.components.LanguageOption
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LanguageScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf(LanguagePreference.getLanguage(context)) }
    var pendingLanguage by remember { mutableStateOf("") }

    var showRestartDialog by remember { mutableStateOf(false) }
    val activity = context as? Activity
    val coroutineScope = rememberCoroutineScope()

    val languages = listOf(
        Triple(stringResource(R.string.system_language), "system", R.drawable.globe),
        Triple("English", "en", R.drawable.flag_uk),
        Triple("Українська", "uk", R.drawable.flag_ukraine)
    )

    BackHandler { onBackClick() }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.language_settings),
                onBackClick = onBackClick,
                showBackButton = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(BasePadding))

            languages.forEachIndexed { index, (label, code, iconRes) ->
                LanguageOption(
                    language = label,
                    id = iconRes,
                    isSelected = selectedLanguage == code,
                    onClick = {
                        pendingLanguage = code
                        showRestartDialog = true
                    }
                )

                if (index != languages.lastIndex) {
                    Spacer(modifier = Modifier.height(BasePadding))
                }
            }
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
}
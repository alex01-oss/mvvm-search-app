package com.loc.searchapp.presentation.language

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.language.components.TopBar
import com.loc.searchapp.utils.LanguagePreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LanguageScreen(
    navigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var selectedLanguage by remember { mutableStateOf(LanguagePreference.getLanguage(context)) }
    var showRestartDialog by remember { mutableStateOf(false) }
    var pendingLanguage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        navigateUp()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        TopBar(onBackClick = navigateUp)

        Spacer(modifier = Modifier.height(MediumPadding1))

        Text(
            text = stringResource(id = R.string.select_language),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = MediumPadding1)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LanguageOption(
            language = stringResource(id = R.string.system_language),
            isSelected = selectedLanguage == "system",
            onClick = {
                pendingLanguage = "system"
                showRestartDialog = true
            },
            id = R.drawable.globe
        )

        Spacer(modifier = Modifier.height(8.dp))

        LanguageOption(
            language = "English",
            isSelected = selectedLanguage == "en",
            onClick = {
                pendingLanguage = "en"
                showRestartDialog = true
            },
            id = R.drawable.flag_uk
        )

        Spacer(modifier = Modifier.height(8.dp))

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
        AlertDialog(
            onDismissRequest = { showRestartDialog = false },
            title = { Text(stringResource(id = R.string.change_language)) },
            text = { Text(stringResource(id = R.string.restart_app_message)) },
            confirmButton = {
                Button(onClick = {
                    showRestartDialog = false
                    LanguagePreference.setLanguage(context, pendingLanguage)
                    selectedLanguage = pendingLanguage

                    coroutineScope.launch {
                        delay(300)
                        activity?.recreate()
                    }
                }) {
                    Text(stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                Button(onClick = { showRestartDialog = false }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun LanguageOption(
    language: String,
    id: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isSystemIcon = id == R.drawable.globe

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MediumPadding1)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = id),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
            colorFilter =
                if (isSystemIcon) ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                else null
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = language,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
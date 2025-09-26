package com.loc.searchapp.presentation.shared.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.auth.AuthField
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.BorderStroke
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.InputFieldHeight
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding2
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.ui.values.Dimens.TextSize
import com.loc.searchapp.presentation.shared.components.notifications.InlineErrorMessage

@Composable
fun AuthForm(
    modifier: Modifier = Modifier,
    title: AnnotatedString,
    fields: List<AuthField>,
    onSubmitClick: () -> Unit,
    isLoading: Boolean,
    submitButtonText: String,
    onBottomTextClick: () -> Unit,
    bottomContent: @Composable () -> Unit,
    showError: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MediumPadding2)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title)
        Spacer(modifier = Modifier.height(BasePadding))

        if (showError != null) InlineErrorMessage(message = showError)

        fields.forEach { field ->
            Column(modifier = Modifier.fillMaxWidth()) {
                if (field.isPassword) {
                    PasswordTextField(
                        value = field.value,
                        placeholder = field.placeholder,
                        onValueChange = field.onValueChange,
                        painterResource = field.icon,
                        isPasswordVisible = field.isPasswordVisible,
                        onPasswordVisibilityChange = { field.onPasswordVisibilityChange?.invoke() },
                        isError = field.isError
                    )
                } else {
                    CustomTextField(
                        value = field.value,
                        placeholder = field.placeholder,
                        onValueChange = field.onValueChange,
                        painterResource = field.icon,
                        isError = field.isError
                    )
                }

                if (field.errorMessage != null) {
                    Text(
                        text = field.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = BasePadding, top = ExtraSmallPadding)
                    )
                }
            }

            Spacer(modifier = Modifier.height(BasePadding))
        }

        Spacer(modifier = Modifier.height(BasePadding))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(InputFieldHeight),
            onClick = onSubmitClick,
            shape = RoundedCornerShape(StrongCorner),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(BasePadding),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = BorderStroke
                )
            } else {
                Text(
                    text = submitButtonText,
                    color = themeAdaptiveColor()
                )
            }
        }

        Spacer(modifier = Modifier.height(BasePadding))

        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(horizontal = BasePadding),
                text = stringResource(id = R.string.or),
                style = TextStyle(
                    fontSize = TextSize,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        TextButton(onClick = onBottomTextClick) {
            bottomContent()
        }
    }
}
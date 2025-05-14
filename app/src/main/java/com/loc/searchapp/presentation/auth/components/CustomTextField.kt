package com.loc.searchapp.presentation.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.loc.searchapp.presentation.Dimens.ExtraSmallPadding2

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    painterResource: Painter,
    placeholder: String,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= 64) onValueChange(it) },
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        singleLine = true,
        maxLines = 1,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor =
                if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary,
            unfocusedBorderColor =
                if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onBackground
        ),
        keyboardOptions = KeyboardOptions.Default,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = ExtraSmallPadding2),
                contentDescription = null,
                tint =
                    if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
            )
        }
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    painterResource: Painter,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {},
    placeholder: String,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= 22) onValueChange(it) },
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        singleLine = true,
        isError = isError,
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor =
                if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary,
            unfocusedBorderColor =
                if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onBackground
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = ExtraSmallPadding2),
                contentDescription = null,
                tint =
                    if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            val iconImage = if (isPasswordVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            IconButton(onClick = { onPasswordVisibilityChange(!isPasswordVisible) }) {
                Icon(imageVector = iconImage, contentDescription = null)
            }
        },
        visualTransformation = if (!isPasswordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None
    )
}
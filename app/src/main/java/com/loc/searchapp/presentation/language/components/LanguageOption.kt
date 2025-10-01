package com.loc.searchapp.presentation.language.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.SmallCorner
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner

@Composable
fun LanguageOption(
    modifier: Modifier = Modifier,
    language: String,
    id: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isSystemIcon = id == R.drawable.globe

    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding)
            .semantics(mergeDescendants = true) {
                role = Role.RadioButton
            }
            .clickable { onClick() }
            .border(
                width = SmallCorner,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                shape = RoundedCornerShape(StrongCorner)
            )
            .padding(BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(IconSize),
            painter = painterResource(id = id),
            contentDescription = null,
            colorFilter =
                if (isSystemIcon) ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                else null
        )

        Spacer(modifier = Modifier.width(BasePadding))

        Text(
            text = language,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
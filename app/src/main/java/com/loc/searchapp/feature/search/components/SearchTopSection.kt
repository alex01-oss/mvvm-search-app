package com.loc.searchapp.feature.search.components

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallCorner
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.navBarHeight
import com.loc.searchapp.core.ui.components.input.SearchBar

@Composable
fun SearchTopSection(
    modifier: Modifier = Modifier,
    searchQuery: String,
    placeholder: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBurgerClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(navBarHeight)
                .clip(RoundedCornerShape(ExtraSmallPadding))
                .burgerButtonBorder(),
            color =
                if (isSystemInDarkTheme()) colorResource(id = R.color.input_background)
                else Color.Transparent,
            tonalElevation = 0.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            IconButton(onClick = onBurgerClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.width(SmallPadding))

        SearchBar(
            modifier = Modifier.weight(1f),
            text = searchQuery,
            placeholder = placeholder,
            readOnly = false,
            onValueChange = onQueryChange,
            onSearch = onSearch
        )
    }
}

fun Modifier.burgerButtonBorder() = composed {
    if (!isSystemInDarkTheme()) {
        border(
            width = ExtraSmallCorner,
            color = Color.Black,
            shape = MaterialTheme.shapes.medium
        )
    } else {
        this
    }
}
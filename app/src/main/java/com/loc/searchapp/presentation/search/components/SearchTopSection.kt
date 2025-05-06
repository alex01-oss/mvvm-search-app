package com.loc.searchapp.presentation.search.components

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.loc.searchapp.presentation.common.components.SearchBar

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
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
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

        Spacer(modifier.width(8.dp))

        SearchBar(
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
            width = 1.dp,
            color = Color.Black,
            shape = MaterialTheme.shapes.medium
        )
    } else {
        this
    }
}
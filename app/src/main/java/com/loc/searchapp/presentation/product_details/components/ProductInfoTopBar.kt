package com.loc.searchapp.presentation.product_details.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.loc.searchapp.R
import com.loc.searchapp.presentation.common.components.SharedTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInfoTopBar(
    onBackClick: () -> Unit,
    onToggleCart: () -> Unit,
) {
    SharedTopBar(
        title = "Product info",
        onBackClick = onBackClick,
        actions = {
            IconButton(
                onClick = onToggleCart
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_shopping_cart),
                    contentDescription = null
                )
            }
        },
        showBackButton = true
    )
}
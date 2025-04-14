package com.loc.searchapp.presentation.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.domain.model.ListItem
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.common.ProductsList

@Composable
fun CartScreen(
    cartItems: State<List<CartItem>>,
    navigateToDetails: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            )
    ) {
        Text(
            text = "Cart",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = colorResource(id = R.color.text_title)
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        val cartListItems = cartItems.value.map { ListItem.CartListItem(it) }

        ProductsList(
            modifier = Modifier.padding(horizontal = MediumPadding1),
            items = cartListItems,
            onClick = { item ->
                if (item is ListItem.CartListItem) navigateToDetails(item.cartItem)
            },
            onRemove = { item ->
                if (item is ListItem.CartListItem) onRemove(item.cartItem)
            },
            onAdd = {},
        )
    }
}
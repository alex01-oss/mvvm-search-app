package com.loc.searchapp.presentation.products_navigator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.BottomNavItem
import com.loc.searchapp.presentation.Dimens.IconSize

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    selected: Int,
    onItemClick: (Int) -> Unit
) {
    val navBarHeight = 56.dp
    val activeButtonSize = 64.dp
    val horizontalPadding = 24.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(navBarHeight + 60.dp)
            .padding(horizontal = 16.dp)
            .padding(bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.bottom_nav)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (index != selected) {
                            IconButton(
                                onClick = { onItemClick(index) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(IconSize),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        items.getOrNull(selected)?.let { selectedItem ->
            Layout(
                content = {
                    Box(
                        modifier = Modifier
                            .size(activeButtonSize)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable { onItemClick(selected) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = selectedItem.icon),
                            contentDescription = null,
                            modifier = Modifier.size(IconSize),
                            tint = Color.White
                        )
                    }
                }
            ) { measurables, constraints ->
                val placeables = measurables.map { it.measure(constraints) }
                val buttonPlaceable = placeables.first()

                val availableWidth = constraints.maxWidth - (2 * horizontalPadding.roundToPx())
                val itemWidth = availableWidth / items.size

                val paddingOffset = horizontalPadding.roundToPx()
                val centerX = paddingOffset + (selected * itemWidth) + (itemWidth / 2)
                val posX = centerX - (buttonPlaceable.width / 2)

                layout(constraints.maxWidth, constraints.maxHeight) {
                    buttonPlaceable.place(
                        posX,
                        constraints.maxHeight - navBarHeight.roundToPx() - (activeButtonSize.roundToPx() - navBarHeight.roundToPx()) / 2
                    )
                }
            }
        }
    }
}
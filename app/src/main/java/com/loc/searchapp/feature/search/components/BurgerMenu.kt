package com.loc.searchapp.feature.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.core.data.remote.dto.MenuResponse
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.BurgerIconSize
import com.loc.searchapp.core.ui.values.Dimens.BurgerPadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.TextSize

@Composable
fun BurgerMenu(
    modifier: Modifier = Modifier,
    menu: MenuResponse?,
    onOpenUrl: (String) -> Unit,
    onNavigateToSearch: (String) -> Unit,
) {
    val expandedIndex = remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier
            .fillMaxSize()
            .padding(BasePadding)
    ) {
        item {
            Text(
                modifier = Modifier.padding(bottom = BasePadding),
                text = stringResource(id = R.string.categories),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (menu == null) {
            item {
                CircularProgressIndicator(
                    modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(MediumPadding1)
                )
            }
        } else {
            listOfNotNull(
                menu.sharpeningTool,
                menu.axialTool,
                menu.grindingTool,
                menu.constructionTool
            ).forEachIndexed { index, category ->
                item {
                    val expanded = expandedIndex.value == index

                    Column(modifier.padding(ExtraSmallPadding)) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        expandedIndex.value =
                                            if (expanded) null
                                            else index
                                    }
                                    .padding(SmallPadding),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = category.title,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector =
                                        if (expanded) Icons.Default.ExpandLess
                                        else Icons.Default.ExpandMore,
                                    contentDescription = null
                                )
                            }
                        }

                        AnimatedVisibility(visible = expanded) {
                            Column(
                                modifier = Modifier.padding(start = SmallPadding, top = ExtraSmallPadding)
                            ) {
                                category.items.forEach { group ->
                                    Text(
                                        text = group.text,
                                        modifier = Modifier.padding(start = SmallPadding, top = ExtraSmallPadding),
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    group.items.forEach { item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clickable {
                                                    when {
                                                        item.type == "button" && item.url != null -> {
                                                            onOpenUrl(item.url)
                                                        }

                                                        item.searchType != null -> {
                                                            onNavigateToSearch(item.searchType)
                                                        }
                                                    }
                                                }
                                                .padding(vertical = BurgerPadding, horizontal = SmallPadding),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val icon = when {
                                                item.url != null -> Icons.Default.PictureAsPdf
                                                item.searchType != null -> Icons.Default.Search
                                                else -> Icons.AutoMirrored.Filled.ArrowRight
                                            }

                                            Icon(
                                                modifier = Modifier.size(BurgerIconSize),
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = when {
                                                    item.url != null -> MaterialTheme.colorScheme.error
                                                    item.searchType != null -> MaterialTheme.colorScheme.primary
                                                    else -> LocalContentColor.current
                                                }
                                            )

                                            Spacer(modifier = Modifier.width(SmallPadding))

                                            Text(
                                                text = item.text,
                                                fontSize = TextSize
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
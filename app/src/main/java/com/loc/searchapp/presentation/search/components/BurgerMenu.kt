package com.loc.searchapp.presentation.search.components

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
import androidx.compose.ui.unit.sp
import com.loc.searchapp.R
import com.loc.searchapp.data.remote.dto.MenuResponse

@Composable
fun BurgerMenu(
    menu: MenuResponse?,
    onOpenUrl: (String) -> Unit,
    onNavigateToSearch: (String) -> Unit,
) {
    val expandedIndex = remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.categories),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (menu == null) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(24.dp)
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

                    Column(
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        expandedIndex.value = if (expanded) null else index
                                    }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category.title,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
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
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            ) {
                                category.items.forEach { group ->
                                    Text(
                                        text = group.text,
                                        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
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
                                                .padding(vertical = 6.dp, horizontal = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val icon = when {
                                                item.url != null -> Icons.Default.PictureAsPdf
                                                item.searchType != null -> Icons.Default.Search
                                                else -> Icons.AutoMirrored.Filled.ArrowRight
                                            }

                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = when {
                                                    item.url != null -> MaterialTheme.colorScheme.error
                                                    item.searchType != null -> MaterialTheme.colorScheme.primary
                                                    else -> LocalContentColor.current
                                                }
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = item.text,
                                                fontSize = 14.sp
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
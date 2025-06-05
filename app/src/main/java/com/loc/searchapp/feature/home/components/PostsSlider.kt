package com.loc.searchapp.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.ArticleImageHeight
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.PagerHeight
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.ui.values.Dimens.TextBarHeight
import com.loc.searchapp.core.utils.Constants.CATALOG_URL
import com.loc.searchapp.feature.shared.model.UiState
import kotlinx.coroutines.launch

@Composable
fun PostsSlider(
    modifier: Modifier = Modifier,
    state: UiState<List<Post>>,
    onPostClick: (Post) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    when(state) {
        UiState.Empty -> EmptyScreen(stringResource(id = R.string.empty_news))
        is UiState.Error -> EmptyScreen(stringResource(id = R.string.error))
        UiState.Loading -> PostsSliderShimmer()
        is UiState.Success -> {
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f,
                pageCount = { state.data.size }
            )
            Box(
                modifier
                    .fillMaxWidth()
                    .height(ArticleImageHeight)
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PagerHeight),
                    state = pagerState
                ) { page ->
                    val post = state.data[page]
                    val fullImageUrl = "$CATALOG_URL${post.imageUrl}"

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(StrongCorner))
                            .clickable { onPostClick(post) }
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = fullImageUrl,
                            contentDescription = post.title,
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .height(TextBarHeight)
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = BasePadding, vertical = SmallPadding),
                            verticalArrangement = Arrangement.Center
                        ) {
                            val currentPost = state.data[pagerState.currentPage]
                            Text(
                                text = currentPost.title,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = HtmlCompat.fromHtml(
                                    currentPost.content,
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                                    .toString(),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (pagerState.currentPage > 0) {
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = SmallPadding)
                                    .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape),
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(
                                            (pagerState.currentPage - 1).coerceAtLeast(
                                                0
                                            )
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        if (pagerState.currentPage < state.data.size - 1) {
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = SmallPadding)
                                    .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape),
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(
                                            (pagerState.currentPage + 1).coerceAtMost(
                                                state.data.size - 1
                                            )
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
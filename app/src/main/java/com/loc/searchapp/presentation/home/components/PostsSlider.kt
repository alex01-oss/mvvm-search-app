package com.loc.searchapp.presentation.home.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.ui.values.Dimens.TextBarHeight
import com.loc.searchapp.core.utils.Constants.BASE_URL
import com.loc.searchapp.presentation.shared.components.notifications.EmptyScreen
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.network.NetworkStatus

@Composable
fun PostsSlider(
    modifier: Modifier = Modifier,
    networkStatus: NetworkStatus,
    state: UiState<List<Post>>,
    onPostClick: (Int) -> Unit
) {
    when {
        networkStatus != NetworkStatus.Available -> {
            EmptyScreen(stringResource(R.string.no_internet_connection))
        }
        state == UiState.Empty -> EmptyScreen(stringResource(id = R.string.empty_news))
        state is UiState.Error -> EmptyScreen(stringResource(id = R.string.error))
        state == UiState.Loading -> PostsSliderShimmer()
        state is UiState.Success -> {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { state.data.size }
            )

            Column(modifier = modifier) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PostImageHeight)
                ) {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(PostImageHeight),
                        state = pagerState
                    ) { page ->
                        val post = state.data[page]
                        val fullImageUrl = "$BASE_URL${post.image}"

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(StrongCorner))
                                .clickable { onPostClick(post.id) }
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
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.8f)
                                            )
                                        )
                                    )
                                    .padding(horizontal = BasePadding, vertical = SmallPadding),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = post.title,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = HtmlCompat.fromHtml(
                                        post.content,
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                    ).toString(),
                                    color = Color.White.copy(alpha = 0.9f),
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                PagerIndicator(
                    totalPages = state.data.size,
                    currentPage = pagerState.currentPage,
                )
            }
        }
    }
}
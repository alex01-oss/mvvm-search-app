package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.loc.searchapp.domain.model.Post
import com.loc.searchapp.presentation.Dimens.ArticleImageHeight
import com.loc.searchapp.presentation.Dimens.BasePadding
import com.loc.searchapp.presentation.Dimens.DefaultCorner
import com.loc.searchapp.presentation.Dimens.PagerHeight
import com.loc.searchapp.presentation.Dimens.PostImageHeight
import com.loc.searchapp.presentation.Dimens.ExtraSmallCorner
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.presentation.Dimens.StrongCorner
import com.loc.searchapp.presentation.Dimens.TextBarHeight
import com.loc.searchapp.utils.Constants.CATALOG_URL
import kotlinx.coroutines.launch

@Composable
fun PostsSlider(
    modifier: Modifier = Modifier,
    posts: List<Post>,
    onPostClick: (Post) -> Unit,
) {
    if (posts.isEmpty()) {
        Box(
            modifier
                .fillMaxWidth()
                .height(PostImageHeight)
                .clip(RoundedCornerShape(StrongCorner))
                .background(Color.Transparent)
                .border(
                    width = ExtraSmallCorner,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(DefaultCorner)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.empty_news),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { posts.size }
    )

    val coroutineScope = rememberCoroutineScope()

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
            val post = posts[page]
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
                    val currentPost = posts[pagerState.currentPage]
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

                if (pagerState.currentPage < posts.size - 1) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = SmallPadding)
                            .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    (pagerState.currentPage + 1).coerceAtMost(
                                        posts.size - 1
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
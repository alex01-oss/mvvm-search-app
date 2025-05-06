package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Post
import com.loc.searchapp.utils.Constants.CATALOG_URL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
    posts: List<Post>,
    onPostClick: (Post) -> Unit
) {
    if (posts.isEmpty()) {
        Box(
            modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(id = R.string.empty_news))
        }
        return
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { posts.size }
    )

    val coroutineScope = rememberCoroutineScope()

//    LaunchedEffect(pagerState.currentPage) {
//        while (true) {
//            delay(3000)
//            val nextPage =
//                if (pagerState.currentPage == posts.size - 1) 0
//                else pagerState.currentPage + 1
//            pagerState.animateScrollToPage(nextPage)
//        }
//    }

    Box(
        modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier.fillMaxSize()
        ) { page ->
            val post = posts[page]
            val fullImageUrl = "$CATALOG_URL${post.imageUrl}"
            Box(
                modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = { onPostClick(post) })
            ) {
                AsyncImage(
                    model = fullImageUrl,
                    contentDescription = post.title,
                    modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier.height(4.dp))
                        Text(
                            text = HtmlCompat.fromHtml(post.content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage((pagerState.currentPage - 1).coerceAtLeast(0))
                        }
                    },
                    modifier
                        .align(Alignment.CenterStart)
                        .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage((pagerState.currentPage + 1).coerceAtMost(posts.size - 1))
                        }
                    },
                    modifier
                        .align(Alignment.CenterEnd)
                        .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}
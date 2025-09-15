package com.loc.searchapp.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.feature.home.player.YouTubeVideoPlayer
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.model.VideoUi
import com.loc.searchapp.feature.shared.network.NetworkStatus
import kotlinx.coroutines.launch

@Composable
fun YouTubeVideoSlider(
    modifier: Modifier = Modifier,
    state: UiState<List<VideoUi>>,
    networkStatus: NetworkStatus,
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    when {
        networkStatus != NetworkStatus.Available -> {
            EmptyScreen(stringResource(R.string.no_internet_connection))
        }

        state == UiState.Empty -> EmptyScreen(stringResource(id = R.string.empty_videos))
        state is UiState.Error -> EmptyScreen(stringResource(id = R.string.error))
        state == UiState.Loading -> VideoSliderShimmer()
        state is UiState.Success -> {
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f,
                pageCount = { state.data.size }
            )

            Box(
                modifier
                    .fillMaxWidth()
                    .height(PostImageHeight)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    YouTubeVideoPlayer(
                        videoId = state.data[page],
                        lifecycleOwner = lifecycleOwner
                    )
                }

                if (pagerState.currentPage > 0) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = SmallPadding)
                            .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.arrow_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                if (pagerState.currentPage < state.data.size - 1) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = SmallPadding)
                            .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(id = R.string.arrow_forward),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
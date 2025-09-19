package com.loc.searchapp.presentation.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.loc.searchapp.R
import com.loc.searchapp.presentation.shared.components.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.model.VideoUi
import com.loc.searchapp.presentation.shared.network.NetworkStatus
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubeVideoSlider(
    modifier: Modifier = Modifier,
    state: UiState<List<VideoUi>>,
    networkStatus: NetworkStatus,
) {
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
                pageCount = { state.data.size }
            )

            Column(modifier = modifier) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PostImageHeight)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(PostImageHeight)
                                .clip(RoundedCornerShape(StrongCorner)),
                            factory = { context ->
                                YouTubePlayerView(context).apply {
                                    lifecycleOwner.lifecycle.addObserver(this)

                                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                        override fun onReady(youTubePlayer: YouTubePlayer) {
                                            state.data[page].videoId.let { id ->
                                                youTubePlayer.mute()
                                                youTubePlayer.cueVideo(id, 0f)
                                            }
                                        }
                                    })
                                }
                            }
                        )
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
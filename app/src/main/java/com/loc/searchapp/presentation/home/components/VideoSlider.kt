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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.presentation.shared.components.notifications.EmptyContent
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
            EmptyContent(
                message = stringResource(R.string.no_internet_connection),
                iconId = R.drawable.ic_network_error,
            )
        }

        state == UiState.Empty -> {
            EmptyContent(
                message = stringResource(id = R.string.empty_videos),
                iconId = R.drawable.ic_empty_videos,
            )
        }

        state is UiState.Error -> {
            EmptyContent(
                message = stringResource(id = R.string.error),
                iconId = R.drawable.ic_network_error,
            )
        }

        state == UiState.Loading -> VideoSliderShimmer()

        state is UiState.Success -> {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { state.data.size }
            )

            val currentPageText = stringResource(
                R.string.page_x_of_y,
                pagerState.currentPage + 1,
                pagerState.pageCount
            )

            val context = LocalContext.current

            Column(modifier = modifier) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PostImageHeight)
                        .semantics {
                            stateDescription = currentPageText
                        }
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(PostImageHeight)
                                .clip(RoundedCornerShape(StrongCorner))
                                .semantics(mergeDescendants = true) {
                                    role = Role.Button
                                    contentDescription = context.getString(
                                        R.string.youtube_video_button_description,
                                        state.data[page].title
                                    )
                                },
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
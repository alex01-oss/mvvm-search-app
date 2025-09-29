package com.loc.searchapp.presentation.home.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.IndicatorSize
import com.loc.searchapp.core.ui.values.Dimens.LogoHeight
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.TitleSize
import com.loc.searchapp.core.ui.values.Dimens.TopLogoHeight
import com.loc.searchapp.presentation.home.components.HomeCategories
import com.loc.searchapp.presentation.home.components.HomeTopBar
import com.loc.searchapp.presentation.home.components.PostsSlider
import com.loc.searchapp.presentation.home.components.SocialIcon
import com.loc.searchapp.presentation.home.components.YouTubeVideoSlider
import com.loc.searchapp.presentation.home.viewmodel.HomeViewModel
import com.loc.searchapp.presentation.shared.network.NetworkObserver
import com.loc.searchapp.presentation.shared.network.NetworkStatus
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.loc.searchapp.presentation.shared.viewmodel.PostViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel,
    viewModel: HomeViewModel,
    onCategoryClick: (Int) -> Unit,
    onPostClick: (Int) -> Unit,
    onAvatarClick: () -> Unit,
    onAllPostsClick: () -> Unit,
) {
    val categoriesState by viewModel.categoriesState.collectAsState()
    val videoIdsState by viewModel.videoState.collectAsState()
    val postsState by postViewModel.recentPostsState.collectAsState()

    val scrollState = rememberLazyListState()

    val scrolled = remember {
        derivedStateOf {
            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = scrollState.firstVisibleItemScrollOffset

            when {
                firstVisibleItemIndex > 0 -> 1f
                firstVisibleItemScrollOffset > 0 -> (firstVisibleItemScrollOffset / 200f)
                    .coerceIn(0f, 1f)

                else -> 0f
            }
        }
    }

    val logoAlpha by animateFloatAsState(
        targetValue = scrolled.value,
        animationSpec = tween(300),
        label = "logoAlpha"
    )

    val context = LocalContext.current
    val networkObserver = remember { NetworkObserver(context) }
    val networkStatus by networkObserver.networkStatus.collectAsState(
        initial = NetworkStatus.Available
    )

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            HomeTopBar(
                viewModel = authViewModel,
                scrollState = scrolled.value,
                logoAlpha = logoAlpha,
                onAvatarClick = onAvatarClick
            )
        },
        content = { paddingValues ->
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = BasePadding)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.height(LogoHeight),
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(id = R.string.logo),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(BasePadding))
                }

                item {
                    val start = stringResource(R.string.about_start)
                    val company = stringResource(R.string.about_company, " ")
                    val end = stringResource(R.string.about_end)

                    Text(
                        text = buildAnnotatedString {
                            append(start)
                            append(" ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(company)
                            }
                            append(end)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }


                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = BasePadding,
                                bottom = SmallPadding
                            ),
                    ) {
                        Text(
                            text = stringResource(id = R.string.categories),
                            fontWeight = FontWeight.Bold,
                            fontSize = TitleSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    HomeCategories(
                        state = categoriesState,
                        onCategoryClick = onCategoryClick,
                        networkStatus = networkStatus,
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = BasePadding),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.last_news),
                            fontWeight = FontWeight.Bold,
                            fontSize = TitleSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        TextButton(
                            onClick = onAllPostsClick,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.all_posts),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(ExtraSmallPadding))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = stringResource(id = R.string.all_posts),
                                modifier = Modifier.size(IndicatorSize)
                            )
                        }
                    }

                    PostsSlider(
                        state = postsState,
                        onPostClick = onPostClick,
                        networkStatus = networkStatus,
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = BasePadding),
                    ) {
                        Text(
                            text = stringResource(id = R.string.our_videos),
                            fontWeight = FontWeight.Bold,
                            fontSize = TitleSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    YouTubeVideoSlider(
                        networkStatus = networkStatus,
                        state = videoIdsState
                    )
                }

                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = BasePadding,
                                    bottom = SmallPadding
                                ),
                        ) {
                            Text(
                                text = stringResource(id = R.string.social_media),
                                fontWeight = FontWeight.Bold,
                                fontSize = TitleSize,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SocialIcon(
                                R.drawable.facebook,
                                link = "https://www.facebook.com/superabrasives.tools"
                            )
                            SocialIcon(
                                R.drawable.instagram,
                                link = "https://www.instagram.com/pdtools/"
                            )
                            SocialIcon(
                                R.drawable.youtube,
                                link = "https://www.youtube.com/channel/UC3tUVI8r3Bfr8hb9-KzfCvw"
                            )
                            SocialIcon(
                                R.drawable.linkedin,
                                link = "https://www.linkedin.com/company/pdtoolssuperabrasives/posts/?feedView=all"
                            )
                            SocialIcon(
                                R.drawable.twitter,
                                link = "https://x.com/PDT73640376"
                            )
                        }
                    }
                }

                item {
                    Box(modifier = Modifier.fillMaxWidth().height(TopLogoHeight))
                }
            }
        }
    )
}
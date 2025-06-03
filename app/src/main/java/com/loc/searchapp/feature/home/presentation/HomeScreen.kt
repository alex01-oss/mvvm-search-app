package com.loc.searchapp.feature.home.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.components.common.AppSnackbar
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding2
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.TitleSize
import com.loc.searchapp.feature.home.components.HomeCategories
import com.loc.searchapp.feature.home.components.HomeTopBar
import com.loc.searchapp.feature.home.components.PostsSlider
import com.loc.searchapp.feature.home.components.SocialIcon
import com.loc.searchapp.feature.home.components.YouTubeVideoSlider
import com.loc.searchapp.feature.shared.components.ProductListShimmer
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.loc.searchapp.feature.shared.viewmodel.HomeViewModel
import com.loc.searchapp.feature.shared.viewmodel.PostViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel,
    viewModel: HomeViewModel,
    onCategoryClick: () -> Unit,
    onPostClick: (Post) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val menu by viewModel.menu.collectAsState()
    val postState by postViewModel.postsState.collectAsState()

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

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            HomeTopBar(
                viewModel = authViewModel,
                scrollState = scrolled.value,
                logoAlpha = logoAlpha
            )
        },
        content = { paddingValues ->
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = MediumPadding1)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(MediumPadding1))
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
                                top = MediumPadding1,
                                bottom = ExtraSmallPadding2
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.categories),
                            fontWeight = FontWeight.Bold,
                            fontSize = TitleSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    HomeCategories(
                        menu = menu,
                        onCategoryClick = { onCategoryClick() }
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = MediumPadding1,
                                bottom = ExtraSmallPadding2
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.last_news),
                            fontWeight = FontWeight.Bold,
                            fontSize = TitleSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    when (postState) {
                        is UiState.Success -> {
                            PostsSlider(
                                posts = (postState as UiState.Success).data,
                                onPostClick = onPostClick
                            )
                        }

                        UiState.Loading -> ProductListShimmer()

                        is UiState.Error -> EmptyScreen((postState as UiState.Error).message)

                        UiState.Empty -> EmptyScreen(message = stringResource(id = R.string.empty_blog))
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = MediumPadding1,
                                bottom = ExtraSmallPadding2
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.our_videos),
                            fontWeight = FontWeight.Bold,
                            fontSize = TitleSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    YouTubeVideoSlider(
                        videoIds = viewModel.videoIds
                    )
                }

                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = MediumPadding1,
                                    bottom = ExtraSmallPadding2
                                ),
                            horizontalArrangement = Arrangement.Center
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
            }

            SnackbarHost(
                modifier = Modifier.padding(bottom = BasePadding),
                hostState = snackbarHostState,
                snackbar = {
                    AppSnackbar(
                        snackbarHostState = snackbarHostState
                    )
                }
            )
        }
    )
}
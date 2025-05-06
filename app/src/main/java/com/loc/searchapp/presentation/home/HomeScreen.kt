package com.loc.searchapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Post
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.PageIndicatorWidth
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.components.AppSnackbar
import com.loc.searchapp.presentation.home.components.HomeCategories
import com.loc.searchapp.presentation.home.components.HomeTopBar
import com.loc.searchapp.presentation.home.components.ImageSlider
import com.loc.searchapp.presentation.home.components.SocialIcon
import com.loc.searchapp.presentation.home.components.YouTubeVideoSlider
import com.loc.searchapp.presentation.posts.PostViewModel

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
    val menu = viewModel.menu.collectAsState().value
    val posts = postViewModel.posts.collectAsState().value

    Box(
        modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(start = MediumPadding1, top = MediumPadding1, end = MediumPadding1)
        ) {
            item {
                HomeTopBar(viewModel = authViewModel)
                Spacer(modifier.height(MediumPadding1))
            }

            item {
                Text(stringResource(id = R.string.main_text))
                Spacer(modifier.height(MediumPadding1))
            }

            item {
                HomeCategories(
                    menu = menu,
                    onCategoryClick = onCategoryClick
                )
                Spacer(modifier.height(MediumPadding1))
            }

            item {
                ImageSlider(
                    posts = posts,
                    onPostClick = onPostClick
                )
                Spacer(modifier.height(PageIndicatorWidth))
            }

            item {
                YouTubeVideoSlider(
                    videoIds = listOf(
                        "6Hv7BELOddo",
                        "fVAF2z6h4Ic"
                    )
                )
                Spacer(modifier.height(MediumPadding1))
            }

            item {
                Row(
                    modifier.fillMaxWidth().padding(16.dp),
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            snackbar = { snackbarData ->
                AppSnackbar(
                    snackbarHostState = snackbarHostState
                )
            }
        )
    }
}
package com.loc.searchapp.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Page
import com.loc.searchapp.presentation.common.components.ProductButton
import com.loc.searchapp.presentation.common.components.ProductTextButton
import com.loc.searchapp.presentation.Dimens.MediumPadding2
import com.loc.searchapp.presentation.Dimens.PageIndicatorWidth
import com.loc.searchapp.presentation.onboarding.components.OnBoardingPage
import com.loc.searchapp.presentation.onboarding.components.PageIndicator
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    event: (OnBoardingEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val pages = listOf(
            Page(
                title = stringResource(id = R.string.first_title),
                description = stringResource(id = R.string.first_description),
                image = R.drawable.onboarding1
            ),
            Page(
                title = stringResource(id = R.string.second_title),
                description = stringResource(id = R.string.second_description),
                image = R.drawable.onboarding2
            ),
            Page(
                title = stringResource(id = R.string.third_title),
                description = stringResource(id = R.string.third_description),
                image = R.drawable.onboarding3
            )
        )

        val pagerState = rememberPagerState(initialPage = 0) {
            pages.size
        }

        val next = stringResource(id = R.string.next)
        val back = stringResource(id = R.string.back)
        val getStarted = stringResource(id = R.string.get_started)

        val buttonState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    0 -> listOf("", next)
                    1 -> listOf(back, next)
                    2 -> listOf(back, getStarted)
                    else -> listOf("", "")
                }
            }
        }

        HorizontalPager(state = pagerState) { index ->
            OnBoardingPage(page = pages[index])
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MediumPadding2)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PageIndicator(
                modifier = Modifier.width(PageIndicatorWidth),
                pageSize = pages.size,
                selectedPage = pagerState.currentPage
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                val scope = rememberCoroutineScope()

                if (buttonState.value[0].isNotEmpty()) {
                    ProductTextButton(
                        text = buttonState.value[0], onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                            }
                        })
                }

                ProductButton(
                    text = buttonState.value[1], onClick = {
                        scope.launch {
                            if (pagerState.currentPage == 2) {
                                event(OnBoardingEvent.SaveAppEntry)
                            } else {
                                pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                            }
                        }
                    })
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))
    }
}
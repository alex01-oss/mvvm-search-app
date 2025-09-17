package com.loc.searchapp.feature.post_details.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.loc.searchapp.R
import com.loc.searchapp.core.data.remote.dto.PostResponse
import com.loc.searchapp.feature.shared.components.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.LargePadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.utils.Constants.BASE_URL
import com.loc.searchapp.feature.post_details.components.PostInfoTopBar
import com.loc.searchapp.feature.post_details.utils.formatDate
import com.loc.searchapp.feature.posts.components.PostDetailedShimmer
import com.loc.searchapp.feature.shared.components.PaddedContent
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@Composable
fun PostDetailedScreen(
    modifier: Modifier = Modifier,
    state: UiState<PostResponse>,
    onEditClick: (PostResponse) -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel,
    authorName: String? = null
) {
    val post = (state as? UiState.Success)?.data

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            if (post != null) {
                PostInfoTopBar(
                    onEditClick = { onEditClick(post) },
                    onBackClick = onBackClick,
                    post = post,
                    authViewModel = authViewModel
                )
            }
        }
    ) { paddingValues ->
        when (state) {
            is UiState.Loading -> {
                PaddedContent(paddingValues) {
                    PostDetailedShimmer()
                }
            }

            is UiState.Error -> {
                PaddedContent(paddingValues) {
                    EmptyScreen(message = state.message)
                }
            }

            UiState.Empty -> {
                PaddedContent(paddingValues) {
                    EmptyScreen(message = stringResource(id = R.string.error))
                }
            }

            is UiState.Success -> {
                val richTextState = remember { RichTextState() }

                LaunchedEffect(post!!.id) {
                    richTextState.setHtml(post.content)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(MediumPadding1)
                ) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.height(SmallPadding))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.author_label,
                                authorName ?: "№${post.userId}"
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = post.createdAt.formatDate(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(MediumPadding1))

                    if (post.image?.isNotEmpty() == true) {
                        AsyncImage(
                            model = "$BASE_URL${post.image}",
                            contentDescription = post.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(StrongCorner))
                        )

                        Spacer(modifier = Modifier.height(MediumPadding1))
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(StrongCorner)),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        RichText(
                            modifier = Modifier
                                .padding(MediumPadding1),
                            state = richTextState
                        )
                    }

                    Spacer(modifier = Modifier.height(LargePadding))
                }
            }
        }
    }
}
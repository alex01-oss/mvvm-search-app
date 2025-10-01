package com.loc.searchapp.presentation.post_details.presentation

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
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.ui.values.Dimens.TopLogoHeight
import com.loc.searchapp.core.utils.Constants.BASE_URL
import com.loc.searchapp.presentation.post_details.components.PostInfoTopBar
import com.loc.searchapp.presentation.post_details.utils.formatDate
import com.loc.searchapp.presentation.posts.components.PostDetailedShimmer
import com.loc.searchapp.presentation.shared.components.notifications.EmptyContent
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@Composable
fun PostDetailedScreen(
    modifier: Modifier = Modifier,
    state: UiState<Post>,
    postId: Int,
    onEditClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel,
    authorName: String? = null
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            PostInfoTopBar(
                postId = postId,
                onEditClick = onEditClick,
                onBackClick = onBackClick,
                authViewModel = authViewModel
            )
        }
    ) { paddingValues ->
        when (state) {
            is UiState.Loading -> PostDetailedShimmer()

            is UiState.Error -> {
                EmptyContent(
                    message = state.message,
                    iconId = R.drawable.ic_network_error,
                )
            }

            UiState.Empty -> {
                EmptyContent(
                    message = stringResource(id = R.string.error),
                    iconId = R.drawable.ic_network_error,
                )
            }

            is UiState.Success -> {
                val post = state.data
                val richTextState = remember { RichTextState() }

                LaunchedEffect(post.id) {
                    richTextState.setHtml(post.content)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(BasePadding)
                ) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.semantics { heading() }
                    )

                    Spacer(Modifier.height(BasePadding))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics(mergeDescendants = true) {},
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

                    Spacer(Modifier.height(BasePadding))

                    if (post.image?.isNotEmpty() == true) {
                        AsyncImage(
                            model = "$BASE_URL${post.image}",
                            contentDescription = post.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(StrongCorner))
                        )

                        Spacer(modifier = Modifier.height(BasePadding))
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(StrongCorner)),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        RichText(
                            modifier = Modifier
                                .padding(BasePadding),
                            state = richTextState
                        )
                    }

                    Spacer(modifier = Modifier.height(TopLogoHeight))
                }
            }
        }
    }
}
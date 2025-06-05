package com.loc.searchapp.feature.post_details.presentation

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.components.common.EmptyScreen
import com.loc.searchapp.core.ui.values.Dimens.LargePadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.utils.Constants.CATALOG_URL
import com.loc.searchapp.feature.post_details.components.PostInfoTopBar
import com.loc.searchapp.feature.post_details.utils.formatDate
import com.loc.searchapp.feature.posts.components.PostDetailedShimmer
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostDetailedScreen(
    modifier: Modifier = Modifier,
    state: UiState<Post>,
    onEditClick: (Post) -> Unit,
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel,
    authorName: String? = null
) {
    when (state) {
        is UiState.Loading -> PostDetailedShimmer()

        is UiState.Error -> EmptyScreen(state.message)

        UiState.Empty -> EmptyScreen("Пост не знайдено")

        is UiState.Success -> {
            val post = state.data
            val fullImageUrl = "$CATALOG_URL${post.imageUrl}"
            val richTextState = remember { RichTextState() }

            LaunchedEffect(post.id) {
                richTextState.setHtml(post.content)
            }

            Scaffold(
                modifier = modifier,
                containerColor = Color.Transparent,
                topBar = {
                    PostInfoTopBar(
                        onEditClick = { onEditClick(post) },
                        onBackClick = onBackClick,
                        post = post,
                        authViewModel = authViewModel
                    )
                }
            ) { paddingValues ->
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
                            text = "Автор: ${authorName ?: "№${post.userId}"}",
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

                    if (post.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = fullImageUrl,
                            contentDescription = post.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
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
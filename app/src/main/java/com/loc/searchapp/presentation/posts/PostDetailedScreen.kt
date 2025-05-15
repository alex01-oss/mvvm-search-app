package com.loc.searchapp.presentation.posts

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.loc.searchapp.domain.model.Post
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.SectionSpacing
import com.loc.searchapp.presentation.Dimens.StrongCorner
import com.loc.searchapp.presentation.posts.components.PostInfoTopBar
import com.loc.searchapp.utils.Constants.CATALOG_URL
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@Composable
fun PostDetailedScreen(
    modifier: Modifier = Modifier,
    post: Post,
    onEditClick: (Post) -> Unit,
    onBackClick: () -> Unit
) {
    val richTextState = RichTextState()
    richTextState.setHtml(post.content)

    val fullImageUrl = "$CATALOG_URL${post.imageUrl}"

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            PostInfoTopBar(
                onEditClick = { onEditClick(post) },
                onBackClick = onBackClick,
                post = post
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(MediumPadding1),
                text = post.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (post.imageUrl.isNotEmpty()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MediumPadding1)
                        .clip(RoundedCornerShape(StrongCorner)),
                    model = fullImageUrl,
                    contentDescription = post.title,
                    contentScale = ContentScale.FillWidth
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MediumPadding1)
                    .clip(RoundedCornerShape(StrongCorner)),
                color = MaterialTheme.colorScheme.surface
            ) {
                RichText(
                    modifier = Modifier.padding(MediumPadding1),
                    state = richTextState
                )
            }

            Spacer(modifier = Modifier.height(SectionSpacing))
        }
    }
}
package com.loc.searchapp.presentation.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.utils.Constants.BASE_URL

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isAdmin: Boolean = false,
    authorName: String? = null,
) {
    val imageUrl = "$BASE_URL${post.image}"
    val typography = MaterialTheme.typography

    val fallback = stringResource(id = R.string.author_fallback, post.userId)
    val displayName = authorName ?: fallback

    val cardDescription = stringResource(R.string.post_card_description, post.title, displayName)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = cardDescription
                role = Role.Button
            }
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(StrongCorner),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(BasePadding)
        ) {
            if (post.image?.isNotEmpty() == true ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = post.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PostImageHeight)
                        .clip(RoundedCornerShape(StrongCorner))
                )
                Spacer(Modifier.height(BasePadding))
            }

            Text(
                text = post.title,
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(BasePadding))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayName,
                    style = typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = post.createdAt.take(10),
                    style = typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(BasePadding))

            Text(
                text = HtmlCompat.fromHtml(post.content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isAdmin) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_post),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
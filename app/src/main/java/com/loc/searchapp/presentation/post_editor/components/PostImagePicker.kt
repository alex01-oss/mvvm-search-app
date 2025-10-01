package com.loc.searchapp.presentation.post_editor.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.PostFormState
import com.loc.searchapp.core.ui.theme.themeAdaptiveColor
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner

@Composable
fun PostImagePicker(
    context: Context,
    formState: PostFormState,
    onImagePickClick: () -> Unit,
    onImageClear: () -> Unit
) {
    val themeColor = themeAdaptiveColor()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(StrongCorner),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(BasePadding)) {
            Text(
                stringResource(R.string.image),
                style = MaterialTheme.typography.titleMedium,
            )

            Button(
                onClick = onImagePickClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(StrongCorner),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = themeColor
                )

                Spacer(Modifier.width(ExtraSmallPadding))

                Text(
                    text = stringResource(R.string.pick_image),
                    color = themeColor
                )
            }

            if (formState.hasImage) {
                Spacer(Modifier.height(BasePadding))

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(formState.previewUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PostImageHeight)
                        .clip(RoundedCornerShape(StrongCorner))
                )

                Spacer(Modifier.height(BasePadding))

                OutlinedButton(
                    onClick = onImageClear,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(StrongCorner),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(ExtraSmallPadding))
                    Text(stringResource(R.string.delete_image))
                }
            }
        }
    }
}
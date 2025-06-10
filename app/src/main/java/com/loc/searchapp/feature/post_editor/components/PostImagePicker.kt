package com.loc.searchapp.feature.post_editor.components

import android.content.Context
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.PostFormState
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.ExtraSmallPadding
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding

@Composable
fun PostImagePicker(
    context: Context,
    formState: PostFormState,
    onImagePickClick: () -> Unit,
    onImageClear: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(SmallPadding)
    ) {
        Column(modifier = Modifier.padding(BasePadding)) {
            Text(
                stringResource(R.string.image),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = SmallPadding)
            )

            Button(
                onClick = onImagePickClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.AddPhotoAlternate,
                    contentDescription = stringResource(id = R.string.pick_image),
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(ExtraSmallPadding))

                Text(
                    text = stringResource(R.string.pick_image),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (formState.hasImage) {
                Spacer(Modifier.height(SmallPadding))

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(formState.previewUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.preview),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PostImageHeight)
                        .clip(RoundedCornerShape(SmallPadding))
                )

                Spacer(Modifier.height(SmallPadding))

                OutlinedButton(
                    onClick = onImageClear,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_image)
                    )
                    Spacer(Modifier.width(ExtraSmallPadding))
                    Text(stringResource(R.string.delete_image))
                }
            }
        }
    }
}
package com.loc.searchapp.presentation.posts

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Post
import com.loc.searchapp.domain.model.PostFormState
import com.loc.searchapp.presentation.Dimens.BasePadding
import com.loc.searchapp.presentation.Dimens.ExtraSmallPadding
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.Dimens.PostImageHeight
import com.loc.searchapp.presentation.Dimens.SmallPadding
import com.loc.searchapp.presentation.Dimens.TitleSize
import com.loc.searchapp.presentation.posts.components.EditorTopBar
import com.loc.searchapp.presentation.posts.components.ErrorDialog
import com.loc.searchapp.presentation.posts.components.RichTextToolbar
import com.loc.searchapp.utils.FileUtil
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun PostEditorScreen(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel,
    post: Post?,
    onFinish: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val richTextState = remember { RichTextState() }

    var isLoading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showImagePicker by remember { mutableStateOf(false) }

    val imageError = stringResource(id = R.string.image_error)
    val titleError = stringResource(id = R.string.title_error)
    val uploadError = stringResource(id = R.string.upload_error)

    var formState by remember { mutableStateOf(PostFormState()) }

    LaunchedEffect(post) {
        post?.let {
            formState = PostFormState(
                title = it.title,
                imageUrl = it.imageUrl,
                content = it.content
            )
            richTextState.setHtml(it.content)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val fileUtil = FileUtil(context)
                val imageFile = fileUtil.getFileFromUri(uri)
                formState = formState.copy(imageFile = imageFile)
                showImagePicker = false
            } catch (e: Exception) {
                errorMessage = imageError + { e.message }
                showErrorDialog = true
            }
        }
    }

    if (showImagePicker) {
        imagePickerLauncher.launch("image/*")
    }

    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }

    val onSaveClick = {
        if (formState.title.isBlank()) {
            errorMessage = titleError
            showErrorDialog = true
        }

        isLoading = true
        val content = richTextState.toHtml()

        fun savePost(finalImageUrl: String?) {
            if (post == null) {
                viewModel.createPost(formState.title, content, finalImageUrl)
            } else {
                viewModel.editPost(
                    post.id.toInt(),
                    formState.title,
                    content,
                    finalImageUrl ?: "",
                )
            }
            isLoading = false
            onFinish()
        }

        formState.imageFile?.let { file ->
            viewModel.uploadImage(file) { response ->
                if (response != null) {
                    savePost(response.url)
                } else {
                    isLoading = false
                    errorMessage = uploadError
                    showErrorDialog = true
                }
            }
        } ?: savePost(formState.imageUrl)
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            EditorTopBar(
                isNewPost = post == null,
                onSaveClick = onSaveClick,
                onCloseClick = onFinish,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when (isLoading) {
            true ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

            false ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                        .padding(horizontal = MediumPadding1)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(BasePadding)
                ) {
                    Spacer(modifier = Modifier.height(MediumPadding1))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = formState.title,
                        onValueChange = { formState = formState.copy(title = it) },
                        label = { Text(stringResource(id = R.string.title)) },
                        singleLine = true
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(SmallPadding)
                    ) {
                        Column(modifier = Modifier.padding(BasePadding)) {
                            Text(
                                modifier = Modifier.padding(bottom = SmallPadding),
                                text = stringResource(id = R.string.image),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = SmallPadding),
                                onClick = { showImagePicker = true }
                            ) {
                                Icon(
                                    Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground
                                )

                                Spacer(modifier = Modifier.width(ExtraSmallPadding))

                                Text(
                                    text = stringResource(id = R.string.pick_image),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            if (formState.hasImage) {
                                Spacer(modifier = Modifier.height(SmallPadding))

                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(PostImageHeight)
                                        .clip(RoundedCornerShape(SmallPadding)),
                                    model = ImageRequest.Builder(context)
                                        .data(formState.previewUri)
                                        .crossfade(true)
                                        .memoryCachePolicy(CachePolicy.ENABLED)
                                        .diskCachePolicy(CachePolicy.ENABLED)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(SmallPadding))

                                OutlinedButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = SmallPadding),
                                    onClick = {
                                        val imageLoader = ImageLoader(context)
                                        val imageUri = formState.previewUri
                                        val request =
                                            ImageRequest.Builder(context).data(imageUri).build()
                                        val key = request.memoryCacheKey
                                        key?.let { imageLoader.memoryCache?.remove(it) }
                                        imageLoader.diskCache?.remove(imageUri.toString())

                                        formState =
                                            formState.copy(imageUrl = null, imageFile = null)
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                    )

                                    Spacer(modifier = Modifier.width(ExtraSmallPadding))

                                    Text(stringResource(id = R.string.delete_image))
                                }
                            }
                        }
                    }

                    RichTextToolbar(richTextState = richTextState)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = PostImageHeight),
                        shape = RoundedCornerShape(SmallPadding),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                    ) {
                        RichTextEditor(
                            modifier = Modifier
                                .padding(end = SmallPadding)
                                .fillMaxWidth()
                                .padding(BasePadding),
                            state = richTextState,
                            colors = RichTextEditorDefaults.richTextEditorColors(
                                textColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.preview),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = TitleSize
                        ),
                        modifier = Modifier.padding(top = BasePadding),
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = SmallPadding),
                        shape = RoundedCornerShape(SmallPadding)
                    ) {
                        RichText(
                            modifier = Modifier.padding(BasePadding),
                            state = richTextState
                        )
                    }
                }
        }
    }
}
package com.loc.searchapp.feature.post_editor.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.Post
import com.loc.searchapp.core.domain.model.posts.PostFormState
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.SmallPadding
import com.loc.searchapp.core.ui.values.Dimens.TitleSize
import com.loc.searchapp.core.utils.FileUtil
import com.loc.searchapp.feature.post_editor.components.EditorTopBar
import com.loc.searchapp.feature.post_editor.components.PostImagePicker
import com.loc.searchapp.feature.post_editor.components.RichTextToolbar
import com.loc.searchapp.feature.shared.components.ErrorDialog
import com.loc.searchapp.feature.shared.model.UiState
import com.loc.searchapp.feature.shared.viewmodel.PostViewModel
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
    postActionState: UiState<Unit>,
    onFinish: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val richTextState = remember { RichTextState() }

    var formState by remember {
        mutableStateOf(PostFormState())
    }

    var showImagePicker by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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

    LaunchedEffect(postActionState) {
        when (postActionState) {
            is UiState.Error -> {
                errorMessage = postActionState.message
                showErrorDialog = true
            }

            is UiState.Success -> {
                onFinish()
                viewModel.resetPostActionState()
            }

            else -> Unit
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
            } catch (e: Exception) {
                errorMessage = context.getString(R.string.image_error) + ": ${e.message}"
                showErrorDialog = true
            }
        }
    }

    if (showImagePicker) {
        imagePickerLauncher.launch("image/*")
        showImagePicker = false
    }

    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }

    val onSaveClick = {
        if (formState.title.isBlank()) {
            errorMessage = context.getString(R.string.title_error)
            showErrorDialog = true
        }

        val content = richTextState.toHtml()

        fun savePost(finalImageUrl: String?) {
            if (post == null) {
                viewModel.createPost(formState.title, content, finalImageUrl)
            } else {
                viewModel.editPost(post.id, formState.title, content, finalImageUrl ?: "")
            }
        }

        formState.imageFile?.let { file ->
            viewModel.uploadImage(file) { response ->
                if (response != null) {
                    savePost(response.url)
                } else {
                    errorMessage = context.getString(R.string.upload_error)
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
        if (postActionState is UiState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }
        } else {
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
                    value = formState.title,
                    onValueChange = { formState = formState.copy(title = it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.title)) },
                    singleLine = true
                )

                PostImagePicker(
                    context = context,
                    formState = formState,
                    onImagePickClick = { showImagePicker = true },
                    onImageClear = {
                        val imageLoader = ImageLoader(context)
                        val imageUri = formState.previewUri
                        val request = ImageRequest.Builder(context).data(imageUri).build()
                        val key = request.memoryCacheKey
                        key?.let { imageLoader.memoryCache?.remove(it) }
                        imageLoader.diskCache?.remove(imageUri.toString())
                        formState = formState.copy(imageUrl = null, imageFile = null)
                    }
                )

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
                            .fillMaxWidth()
                            .padding(BasePadding),
                        state = richTextState,
                        colors = RichTextEditorDefaults.richTextEditorColors()
                    )
                }

                Text(
                    text = stringResource(R.string.preview),
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
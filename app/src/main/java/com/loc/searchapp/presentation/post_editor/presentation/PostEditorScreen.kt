package com.loc.searchapp.presentation.post_editor.presentation

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
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.request.ImageRequest
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.posts.PostFormState
import com.loc.searchapp.core.ui.values.Dimens.AboutLogoSize
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.PostImageHeight
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.loc.searchapp.core.utils.FileUtil
import com.loc.searchapp.presentation.post_editor.components.EditorTopBar
import com.loc.searchapp.presentation.post_editor.components.PostImagePicker
import com.loc.searchapp.presentation.post_editor.components.RichTextToolbar
import com.loc.searchapp.presentation.post_editor.model.PostEditorState
import com.loc.searchapp.presentation.shared.components.notifications.AppDialog
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.viewmodel.PostViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults


@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun PostEditorScreen(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel,
    editorState: PostEditorState,
    actionState: UiState<Unit>,
    onFinish: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val richTextState = remember { RichTextState() }

    var formState by remember { mutableStateOf(PostFormState()) }
    var showImagePicker by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(editorState) {
        when (editorState) {
            is PostEditorState.EditMode -> {
                val post = editorState.post
                formState = formState.copy(
                    title = post.title,
                    imageUrl = post.image
                )
                richTextState.setHtml(post.content)
            }

            is PostEditorState.CreateMode -> {
                formState = PostFormState()
                richTextState.clear()
            }

            else -> {}
        }
    }

    LaunchedEffect(actionState) {
        when (actionState) {
            is UiState.Error -> {
                errorMessage = actionState.message
                showErrorDialog = true
            }

            is UiState.Success -> {
                viewModel.resetPostActionState()
                onFinish()
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

    LaunchedEffect(showImagePicker) {
        if (showImagePicker) {
            imagePickerLauncher.launch("image/*")
            showImagePicker = false
        }
    }

    if (showErrorDialog) {
        AppDialog(
            message = errorMessage,
            onConfirm = { showErrorDialog = false },
            onDismiss = { showErrorDialog = false },
            title = stringResource(id = R.string.error),
            confirmLabel = stringResource(id = R.string.ok)
        )
    }

    val onSaveClick = {
        if (formState.title.isBlank()) {
            errorMessage = context.getString(R.string.title_error)
            showErrorDialog = true
        } else {
            val content = richTextState.toHtml()

            fun savePost(finalImageUrl: String?) {
                when (editorState) {
                    is PostEditorState.CreateMode -> {
                        viewModel.createPost(formState.title, content, finalImageUrl)
                    }

                    is PostEditorState.EditMode -> {
                        viewModel.editPost(
                            editorState.post.id,
                            formState.title,
                            content,
                            finalImageUrl ?: ""
                        )
                    }

                    else -> {}
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
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            EditorTopBar(
                isNewPost = editorState is PostEditorState.CreateMode,
                onSaveClick = onSaveClick,
                onCloseClick = onFinish,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when {
            actionState is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                }
            }

            editorState is PostEditorState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                }
            }

            editorState is PostEditorState.Error -> {
                AppDialog(
                    message = editorState.message,
                    onConfirm = onBackClick,
                    title = stringResource(id = R.string.error),
                    confirmLabel = stringResource(id = R.string.ok)
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                        .padding(horizontal = BasePadding)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(BasePadding)
                ) {
                    Spacer(modifier = Modifier.height(BasePadding))

                    OutlinedTextField(
                        value = formState.title,
                        onValueChange = { formState = formState.copy(title = it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(id = R.string.title)) },
                        singleLine = true,
                        shape = RoundedCornerShape(StrongCorner),
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
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                        shape = RoundedCornerShape(StrongCorner),
                        elevation = CardDefaults.cardElevation(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        RichTextEditor(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(BasePadding)
                                .semantics {
                                    contentDescription = context.getString(
                                        R.string.post_content_editor
                                    )
                                },
                            state = richTextState,
                            colors = RichTextEditorDefaults.richTextEditorColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                cursorColor = MaterialTheme.colorScheme.onBackground,
                                textColor = MaterialTheme.colorScheme.onBackground,
                                placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(AboutLogoSize))
                }
            }
        }
    }
}
package com.loc.searchapp.presentation.posts

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.loc.searchapp.domain.model.Post
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditorScreen(
    viewModel: PostViewModel,
    post: Post? = null,
    onFinish: () -> Unit
) {
    val context = LocalContext.current

    var imageUrl by remember { mutableStateOf(post?.imageUrl) }
    var title by remember { mutableStateOf(post?.title) }
    var isLoading by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }
    var imageFile by remember { mutableStateOf<File?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val richTextState = remember { RichTextState() }

    LaunchedEffect(post?.content) {
        if (post?.content?.isNotEmpty() == true) {
            richTextState.setHtml(post.content)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val fileUtil = FileUtil(context)
                val file = fileUtil.getFileFromUri(uri)
                imageFile = file
                showImagePicker = false
                isLoading = true

                viewModel.uploadImage(file) { response ->
                    isLoading = false
                    if (response != null) {
                        imageUrl = response.url
                    } else {
                        errorMessage = "Не вдалося завантажити зображення" // translate
                        showErrorDialog = true
                    }
                }

            } catch (e: Exception) {
                isLoading = false
                errorMessage = "Помилка при обробці зображення: ${e.message}" // translate
                showErrorDialog = true
            }
        }
    }

    val onSaveClick = {
        if (title?.isBlank() == true) {
            errorMessage = "Заголовок не може бути порожнім" // translate
            showErrorDialog = true
        } else {
            isLoading = true

            if (post == null) {
                viewModel.createPost(title.toString(), richTextState.toHtml(), imageUrl)
            } else {
                viewModel.editPost(
                    post.id.toInt(),
                    imageUrl.toString(), title.toString(), richTextState.toHtml()
                )
            }

            isLoading = false
            onFinish()
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Помилка") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showImagePicker) {
        imagePickerLauncher.launch("image/*")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (post == null) "Створення новини" else "Редагування новини") // translate
                },
                actions = {
                    IconButton(onClick = onSaveClick) {
                        Icon(Icons.Default.Save, contentDescription = null)
                    }
                    IconButton(onClick = onFinish) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title.toString(),
                onValueChange = { title = it },
                label = { Text("Title") }, // translate
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Зображення", // translate
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(
                        onClick = { showImagePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Вибрати зображення") // translate
                    }

                    if (imageUrl?.isNotEmpty() == true) {
                        Spacer(modifier = Modifier.height(8.dp))

                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = { imageUrl = "" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Видалити зображення")
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Bold
                    FilledTonalIconButton(onClick = {
                        richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    }) {
                        Icon(Icons.Default.FormatBold, contentDescription = "Жирний")
                    }

                    // Italic
                    FilledTonalIconButton(onClick = {
                        richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    }) {
                        Icon(Icons.Default.FormatItalic, contentDescription = "Курсив")
                    }

                    // Underline
                    FilledTonalIconButton(onClick = {
                        richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                    }) {
                        Icon(Icons.Default.FormatUnderlined, contentDescription = "Підкреслений")
                    }

                    HorizontalDivider(
                        Modifier
                            .height(24.dp)
                            .width(1.dp)
                            .align(Alignment.CenterVertically),
                    )

                    // Bulleted list
                    FilledTonalIconButton(onClick = {
                        richTextState.toggleUnorderedList()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.FormatListBulleted,
                            contentDescription = null
                        )
                    }

                    // Numbered list
                    FilledTonalIconButton(onClick = {
                        richTextState.toggleOrderedList()
                    }) {
                        Icon(
                            Icons.Default.FormatListNumbered,
                            contentDescription = null
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp)
                            .align(Alignment.CenterVertically),
                    )

                    // Align left
                    FilledTonalIconButton(onClick = {
                        richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.FormatAlignLeft,
                            contentDescription = "По лівому краю"
                        )
                    }

                    // Align center
                    FilledTonalIconButton(onClick = {
                        richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                    }) {
                        Icon(Icons.Default.FormatAlignCenter, contentDescription = "По центру")
                    }

                    // Align right
                    FilledTonalIconButton(onClick = {
                        richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.FormatAlignRight,
                            contentDescription = "По правому краю"
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                RichTextEditor(
                    state = richTextState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = RichTextEditorDefaults.richTextEditorColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Text(
                text = "Preview content", // translate
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(top = 16.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                RichText(
                    state = richTextState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

class FileUtil(private val context: Context) {
    fun getFileFromUri(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("image", ".jpg", context.cacheDir)

        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return tempFile
    }
}
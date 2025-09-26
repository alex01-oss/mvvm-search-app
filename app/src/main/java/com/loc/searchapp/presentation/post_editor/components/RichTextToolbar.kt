package com.loc.searchapp.presentation.post_editor.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.core.ui.values.Dimens.BasePadding
import com.loc.searchapp.core.ui.values.Dimens.StrongCorner
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun RichTextToolbar(
    modifier: Modifier = Modifier,
    richTextState: RichTextState
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(StrongCorner),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        val scrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichTextButton(Icons.Default.FormatBold) {
                richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            }

            RichTextButton(Icons.Default.FormatItalic) {
                richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            }

            RichTextButton(Icons.Default.FormatUnderlined) {
                richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            }

            RichTextButton(Icons.AutoMirrored.Filled.FormatListBulleted) {
                richTextState.toggleUnorderedList()
            }

            RichTextButton(Icons.Default.FormatListNumbered) {
                richTextState.toggleOrderedList()
            }

            RichTextButton(Icons.AutoMirrored.Filled.FormatAlignLeft) {
                richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
            }

            RichTextButton(Icons.Default.FormatAlignCenter) {
                richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
            }

            RichTextButton(Icons.AutoMirrored.Filled.FormatAlignRight) {
                richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
            }
        }
    }
}

@Composable
fun RichTextButton(icon: ImageVector, onClick: () -> Unit) {
    FilledTonalIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(
            icon,
            contentDescription = stringResource(id = R.string.editor_button)
        )
    }
}

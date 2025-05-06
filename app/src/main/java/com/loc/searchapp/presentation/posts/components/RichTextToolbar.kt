package com.loc.searchapp.presentation.posts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState

@Composable
fun RichTextToolbar(
    modifier: Modifier = Modifier,
    richTextState: RichTextState
) {
    Card(
        modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Bold
            FilledTonalIconButton(onClick = {
                richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            }) {
                Icon(Icons.Default.FormatBold, contentDescription = null)
            }

            // Italic
            FilledTonalIconButton(onClick = {
                richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            }) {
                Icon(Icons.Default.FormatItalic, contentDescription = null)
            }

            // Underline
            FilledTonalIconButton(onClick = {
                richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            }) {
                Icon(Icons.Default.FormatUnderlined, contentDescription = null)
            }

            HorizontalDivider(
                modifier
                    .height(24.dp)
                    .width(1.dp)
                    .align(Alignment.CenterVertically),
            )

            // Bulleted list
            FilledTonalIconButton(onClick = {
                richTextState.toggleUnorderedList()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.FormatListBulleted, contentDescription = null
                )
            }

            // Numbered list
            FilledTonalIconButton(onClick = {
                richTextState.toggleOrderedList()
            }) {
                Icon(
                    Icons.Default.FormatListNumbered, contentDescription = null
                )
            }

            HorizontalDivider(
                modifier
                    .height(24.dp)
                    .width(1.dp)
                    .align(Alignment.CenterVertically),
            )

            // Align left
            FilledTonalIconButton(onClick = {
                richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
            }) {
                Icon(Icons.AutoMirrored.Filled.FormatAlignLeft, contentDescription = null)
            }

            // Align center
            FilledTonalIconButton(onClick = {
                richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
            }) {
                Icon(Icons.Default.FormatAlignCenter, contentDescription = null)
            }

            // Align right
            FilledTonalIconButton(onClick = {
                richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.FormatAlignRight, contentDescription = null
                )
            }
        }
    }
}
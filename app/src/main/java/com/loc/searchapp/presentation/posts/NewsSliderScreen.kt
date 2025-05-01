package com.loc.searchapp.presentation.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.loc.searchapp.domain.model.Post
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText

@Composable
fun NewsSliderScreen(
    news: List<Post> = emptyList(),
    onNewsClick: (Post) -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { news.size })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Останні новини",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (news.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Новини відсутні")
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .padding(horizontal = 16.dp)
            ) { page ->
                val post = news[page]
                NewsCard(
                    post = post,
                    onClick = { onNewsClick(post) }
                )
            }

            PagerIndicator(
                pageCount = news.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun NewsCard(
    post: Post,
    onClick: () -> Unit
) {
    val richTextState = RichTextState()
    richTextState.setHtml(post.content)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = post.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(150.dp)
            ) {
                RichText(
                    state = richTextState,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Button(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Text("Читати далі")
            }
        }
    }
}

@Composable
fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(12.dp)
                    .clip(RoundedCornerShape(50))
                    .surface(color, RoundedCornerShape(50), contentColorFor(color))
            )
        }
    }
}

@Composable
private fun Modifier.surface(
    color: Color,
    shape: RoundedCornerShape,
    contentColor: Color
): Modifier {
    return this.then(
        Modifier.surface(
            shape = shape,
            color = color,
            contentColor = contentColor
        )
    )
}

@Preview(showBackground = true)
@Composable
fun NewsSliderScreenPreview() {
    MaterialTheme {
        NewsSliderScreen(
            news = listOf(
                Post(
                    id = "1",
                    title = "Новий продукт запущено!",
                    content = "<p>Ми раді повідомити про запуск нашого нового продукту, який змінить спосіб взаємодії з технологіями...</p>",
                    imageUrl = "https://example.com/image1.jpg",
                    createdAt = TODO(),
                    updatedAt = TODO(),
                    userId = TODO()
                ),
                Post(
                    id = "2",
                    title = "Оновлення програми доступне",
                    content = "<p>Велике оновлення нашої програми вже доступне. Серед нових функцій:</p><ul><li>Темна тема</li><li>Кращий користувацький інтерфейс</li></ul>",
                    imageUrl = "https://example.com/image2.jpg",
                    createdAt = TODO(),
                    updatedAt = TODO(),
                    userId = TODO()
                ),
                Post(
                    id = "3",
                    title = "Запрошуємо на конференцію",
                    content = "<p>Приєднуйтесь до нашої щорічної <b>конференції</b>, яка відбудеться наступного місяця.</p>",
                    imageUrl = "https://example.com/image3.jpg",
                    createdAt = TODO(),
                    updatedAt = TODO(),
                    userId = TODO()
                )
            )
        )
    }
}
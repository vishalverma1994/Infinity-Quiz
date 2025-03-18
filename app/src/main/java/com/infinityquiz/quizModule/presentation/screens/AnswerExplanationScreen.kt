package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.infinityquiz.quizModule.domain.model.Content

/**
 * [AnswerExplanationScreen] displays the explanation of an answer, which can include images and text.
 *
 * This composable function takes a list of [Content] objects representing the solution and a callback
 * function for handling the share action. It iterates through the solution content and renders it
 * appropriately based on its `contentType`. Images are displayed using [AsyncImage], and text content
 * is rendered using [Text] after being parsed from HTML.
 *
 * @param solution A list of [Content] objects representing the solution. Each [Content] object
 *                  contains the `contentType` (e.g., "image", "text") and `contentData` which is
 *                  the actual content (e.g., image URL, HTML string). This parameter can be null.
 * @param onShareClick A lambda function that is called when the "Share" button is clicked.
 */
@Composable
fun AnswerExplanationScreen(
    solution: List<Content>?,
    onShareClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Handle the solution based on its contentType
            solution?.forEach { content ->
                when (content.contentType) {
                    "image" -> {
                        AsyncImage(
                            model = content.contentData,
                            contentDescription = "Solution Image",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    else -> {
                        val htmlText = HtmlCompat.fromHtml(content.contentData, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        Text(text = htmlText.toString())
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = onShareClick) {
                Text("Share")
            }
        }
    }
}


@Preview
@Composable
fun AnswerExplanationScreenPreview() {
    AnswerExplanationScreen(listOf(Content("text", "The capital of France is Paris."))) { }
}
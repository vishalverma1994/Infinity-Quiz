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
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.infinityquiz.quizModule.domain.model.Content

@Composable
fun AnswerExplanationScreen(
    solution: List<Content>?,
    onShareClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onShareClick) {
                Text("Share")
            }
        }
    }
}

//
//@Preview
//@Composable
//fun AnswerExplanationScreenPreview() {
//    AnswerExplanationScreen(true, emptyList(), {}, {}, false)
//}
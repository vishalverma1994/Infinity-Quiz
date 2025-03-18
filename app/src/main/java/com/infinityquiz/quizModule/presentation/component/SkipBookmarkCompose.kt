package com.infinityquiz.quizModule.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.infinityquiz.R

@Composable
fun skipBookMarkQuestion(
    isAnswerState: Boolean,
    onSkip: () -> Unit,
    onBookmarkClick: () -> Unit,
    isBookmarked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBookmarkClick) {
            Icon(
                painter = painterResource(if (isBookmarked) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark_unselected),
                contentDescription = if (isBookmarked) "Unbookmark" else "Bookmark"
            )
        }
        Button(onClick = onSkip) {
            Text(text = if(isAnswerState)"Next" else "Skip")
        }
    }
}
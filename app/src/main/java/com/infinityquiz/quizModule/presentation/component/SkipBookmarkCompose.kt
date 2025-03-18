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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinityquiz.R

/**
 * A composable function that displays a row with a bookmark icon and a skip/next button.
 *
 * This function provides controls for skipping a question and bookmarking it.
 * The bookmark icon's appearance changes based on whether the question is bookmarked.
 * The button's text changes between "Skip" and "Next" based on the `isAnswerState`.
 *
 * @param isAnswerState Boolean indicating whether the answer state is active (e.g., the answer is shown).
 *                      If true, the button will display "Next"; otherwise, it will display "Skip".
 * @param onSkip Lambda function invoked when the skip/next button is clicked.
 *                 It should handle the logic for moving to the next question or skipping the current one.
 * @param onBookmarkClick Lambda function invoked when the bookmark icon is clicked.
 *                        It should handle the logic for bookmarking or unbookmarking the question.
 * @param isBookmarked Boolean indicating whether the current question is bookmarked.
 *                     Determines the icon displayed (selected or unselected bookmark).
 */
@Composable
fun SkipBookMarkQuestion(
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

@Preview(showSystemUi = true)
@Composable
private fun SkipBookMarkQuestionPreview() {
    SkipBookMarkQuestion(true, {}, {}, true)
}
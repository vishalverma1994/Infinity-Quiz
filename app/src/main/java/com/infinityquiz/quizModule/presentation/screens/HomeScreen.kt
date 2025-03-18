package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinityquiz.quizModule.presentation.component.CountrySelectionSection
import com.infinityquiz.quizModule.util.NavigationModeUtil
import com.infinityquiz.ui.theme.InfinityQuizTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onQuizStart: (String) -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            CountrySelectionSection()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { onQuizStart.invoke(NavigationModeUtil.START.name) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Start Quiz")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { onQuizStart.invoke(NavigationModeUtil.BOOKMARK.name) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Solve Bookmarks")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    InfinityQuizTheme {
        HomeScreen()
    }
}
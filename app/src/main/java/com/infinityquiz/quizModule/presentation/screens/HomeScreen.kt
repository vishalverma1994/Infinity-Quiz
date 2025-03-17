package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinityquiz.quizModule.presentation.component.CountrySelectionSection
import com.infinityquiz.ui.theme.InfinityQuizTheme

@Composable
fun HomeScreen(
    screenState: HomeScreenState,
    modifier: Modifier = Modifier,

) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (screenState.isLoading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CountrySelectionSection()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    InfinityQuizTheme {
        HomeScreen(
            HomeScreenState()
        )
    }
}
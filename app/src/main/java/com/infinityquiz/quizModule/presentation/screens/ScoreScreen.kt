package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScoreScreen(correctScore: Int, totalScore: Int, onContinueClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Awesome you got", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(40.dp))
        Row {
            Text(
                text = "$correctScore",
                style = MaterialTheme.typography.displayMedium.copy(color = Color.Green),
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = "/$totalScore",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                modifier = Modifier.alignByBaseline()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Correct", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onContinueClick) {
            Text(text = "Continue")
        }
    }
}

@Preview
@Composable
fun ScoreScreenPreview() {
//    ScoreScreen(onContinueClick = {}, viewModel =)
}